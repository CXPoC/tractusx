resource "azurerm_kubernetes_cluster" "main" {
  name                    = var.cluster_name == null ? "${var.prefix}-aks" : var.cluster_name
  kubernetes_version      = var.kubernetes_version
  location                = var.location
  resource_group_name     = var.resource_group_name
  node_resource_group     = var.node_resource_group
  dns_prefix              = var.prefix
  sku_tier                = var.sku_tier
  private_cluster_enabled = var.private_cluster_enabled

  linux_profile {
    admin_username = var.admin_username

    ssh_key {
      key_data = "${file(var.public_ssh_key)}"
    }
  }

  dynamic "default_node_pool" {
    for_each = var.enable_auto_scaling == true ? [] : ["default_node_pool_manually_scaled"]
    content {
      orchestrator_version  = var.orchestrator_version
      name                  = var.agents_pool_name
      node_count            = var.agents_count
      vm_size               = var.agents_size
      os_disk_size_gb       = var.os_disk_size_gb
      vnet_subnet_id        = var.vnet_subnet_id
      enable_auto_scaling   = var.enable_auto_scaling
      max_count             = null
      min_count             = null
      enable_node_public_ip = var.enable_node_public_ip
      availability_zones    = var.agents_availability_zones
      node_labels           = var.agents_labels
      type                  = var.agents_type
      tags                  = merge(var.tags, var.agents_tags)
      max_pods              = var.agents_max_pods
    }
  }

  dynamic "default_node_pool" {
    for_each = var.enable_auto_scaling == true ? ["default_node_pool_auto_scaled"] : []
    content {
      orchestrator_version  = var.orchestrator_version
      name                  = var.agents_pool_name
      vm_size               = var.agents_size
      os_disk_size_gb       = var.os_disk_size_gb
      vnet_subnet_id        = var.vnet_subnet_id
      enable_auto_scaling   = var.enable_auto_scaling
      max_count             = var.agents_max_count
      min_count             = var.agents_min_count
      enable_node_public_ip = var.enable_node_public_ip
      availability_zones    = var.agents_availability_zones
      node_labels           = var.agents_labels
      type                  = var.agents_type
      tags                  = merge(var.tags, var.agents_tags)
      max_pods              = var.agents_max_pods
    }
  }

  dynamic "service_principal" {
    for_each = var.client_id != "" && var.client_secret != "" ? ["service_principal"] : []
    content {
      client_id     = var.client_id
      client_secret = var.client_secret
    }
  }

  dynamic "identity" {
    for_each = var.client_id == "" || var.client_secret == "" ? ["identity"] : []
    content {
      type = "SystemAssigned"
    }
  }

  addon_profile {
    http_application_routing {
      enabled = var.enable_http_application_routing
    }

    kube_dashboard {
      enabled = var.enable_kube_dashboard
    }

    azure_policy {
      enabled = var.enable_azure_policy
    }

    oms_agent {
      enabled                    = var.enable_log_analytics_workspace
      log_analytics_workspace_id = var.enable_log_analytics_workspace ? var.log_analytics_workspace_id : null
    }
  }

  role_based_access_control {
    enabled = var.enable_role_based_access_control

    dynamic "azure_active_directory" {
      for_each = var.enable_role_based_access_control && var.rbac_aad_managed ? ["rbac"] : []
      content {
        managed                = true
        tenant_id              = var.rbac_aad_tenant_id
        admin_group_object_ids = var.rbac_aad_admin_group_object_id != null ? [var.rbac_aad_admin_group_object_id] : []
      }
    }

    dynamic "azure_active_directory" {
      for_each = var.enable_role_based_access_control && !var.rbac_aad_managed ? ["rbac"] : []
      content {
        managed           = false
        client_app_id     = var.rbac_aad_client_app_id
        server_app_id     = var.rbac_aad_server_app_id
        server_app_secret = var.rbac_aad_server_app_secret
      }
    }
  }

  network_profile {
    network_plugin     = var.network_plugin
    network_policy     = var.network_policy
    dns_service_ip     = var.net_profile_dns_service_ip
    docker_bridge_cidr = var.net_profile_docker_bridge_cidr
    outbound_type      = var.net_profile_outbound_type
    pod_cidr           = var.net_profile_pod_cidr
    service_cidr       = var.net_profile_service_cidr
  }

  tags = var.tags
}

resource "azurerm_log_analytics_solution" "main" {
  count                 = var.enable_log_analytics_workspace ? 1 : 0
  solution_name         = "ContainerInsights"
  location              = var.location
  resource_group_name   = var.resource_group_name
  workspace_resource_id = var.log_analytics_workspace_id
  workspace_name        = var.log_analytics_workspace_name

  plan {
    publisher = "Microsoft"
    product   = "OMSGallery/ContainerInsights"
  }

  tags = var.tags
}

# K8S cluster-admin role binding to AAD user
resource "kubernetes_cluster_role_binding" "main" {
  metadata {
    name = "${var.cluster_name}admins"
  }
  role_ref {
    api_group = "rbac.authorization.k8s.io"
    kind = "ClusterRole"
    name = "cluster-admin"
  }

  dynamic "subject" {
    for_each = var.rbac_aad_admin_user_name != null ? ["subject"] : []
    content {
      kind      = "User"
      name      = var.rbac_aad_admin_user_name
      api_group = "rbac.authorization.k8s.io"
    }
  }

  dynamic "subject" {
    for_each = var.rbac_aad_admin_group_object_id != null ? ["subject"] : []
    content {
      kind      = "Group"
      name      = var.rbac_aad_admin_group_object_id
      api_group = "rbac.authorization.k8s.io"
    }
  }

  depends_on = [
    azurerm_kubernetes_cluster.main
  ]
}

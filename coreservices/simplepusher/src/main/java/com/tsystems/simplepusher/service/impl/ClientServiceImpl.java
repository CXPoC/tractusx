package com.tsystems.simplepusher.service.impl;

import com.tsystems.simplepusher.service.ClientService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Clients service.
 */
@Service
public class ClientServiceImpl implements ClientService {

    //ToDo: to db.
    private final Map<UUID, List<String>> uuidMap = new HashMap<>();

    @Override
    public List<String> getAllPublicClients(UUID uuid) {
        return Optional.ofNullable(uuidMap.get(uuid)).orElse(Collections.emptyList());
    }

    @Override
    //ToDo: fix dummy code
    public void addClientToResource(UUID uuid, String uri) {
        uuidMap.put(uuid, Arrays.asList(uri));
    }
}

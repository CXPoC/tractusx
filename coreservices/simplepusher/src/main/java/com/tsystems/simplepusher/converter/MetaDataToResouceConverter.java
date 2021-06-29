package com.tsystems.simplepusher.converter;

import com.tsystems.simplepusher.client.model.ResourceMetadata;
import com.tsystems.simplepusher.model.ids.IdsConnectorDescription;
import de.fraunhofer.iais.eis.*;
import de.fraunhofer.iais.eis.ids.jsonld.Serializer;
import de.fraunhofer.iais.eis.util.ConstraintViolationException;
import de.fraunhofer.iais.eis.util.TypedLiteral;
import de.fraunhofer.iais.eis.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Original converting can be seen in {@link de.fraunhofer.isst.ids.framework.util.IDSUtils}
 */
@Component
@RequiredArgsConstructor
public class MetaDataToResouceConverter {

    /**
     * Serializer of fraunhofer.
     */
    private final Serializer serializer;

    /**
     * Converts resourcemetadata to resouce
     * The original code can be seen at {@link de.fraunhofer.isst.ids.framework.util.IDSUtils}
     *
     * @param metadata  metadata
     * @param connector connector information
     * @param uuid      UUID of resource
     * @return converter {@link Resource}
     */
    public Resource convert(ResourceMetadata metadata, IdsConnectorDescription connector, UUID uuid) {
        // Get the list of keywords
        var keywords = new ArrayList<TypedLiteral>();
        if (metadata.getKeywords() != null) {
            for (var keyword : metadata.getKeywords()) {
                keywords.add(new TypedLiteral(keyword, "EN"));
            }
        }

        // Get the list of representations
        var representations = new ArrayList<Representation>();
        if (metadata.getRepresentations() != null) {
            for (var representation : metadata.getRepresentations().values()) {
                try {
                    representations.add(new RepresentationBuilder(URI.create(
                            "https://w3id.org/idsa/autogen/representation/" + representation.getUuid()))
                            ._language_(Language.EN)
                            ._mediaType_(new IANAMediaTypeBuilder()
                                    ._filenameExtension_(representation.getType())
                                    .build())
                            ._instance_(Util.asList(new ArtifactBuilder(URI.create(
                                    "https://w3id.org/idsa/autogen/artifact/" + representation.getUuid()))
                                    ._byteSize_(BigInteger.valueOf(representation.getByteSize()))
                                    ._fileName_(representation.getName())
                                    .build()))
                            .build());
                } catch (ConstraintViolationException exception) {
                    throw new RuntimeException("Failed to build resource representation.",
                            exception);
                }
            }
        }

        // Get the list of contracts.
        var contracts = new ArrayList<ContractOffer>();
        if (metadata.getPolicy() != null) {
            try {
                // Add the provider to the contract offer.
                final var contractOffer = serializer
                        .deserialize(metadata.getPolicy(), ContractOffer.class);
                contracts.add(new ContractOfferBuilder()
                        ._permission_(contractOffer.getPermission())
                        ._prohibition_(contractOffer.getProhibition())
                        ._obligation_(contractOffer.getObligation())
                        ._contractStart_(contractOffer.getContractStart())
                        ._contractDate_(contractOffer.getContractDate())
                        ._consumer_(contractOffer.getConsumer())
                        ._provider_(connector.getId())
                        ._contractEnd_(contractOffer.getContractEnd())
                        ._contractAnnex_(contractOffer.getContractAnnex())
                        ._contractDocument_(contractOffer.getContractDocument())
                        .build());
            } catch (IOException exception) {
                throw new RuntimeException("Could not deserialize contract.", exception);
            }
        }

        // Build the connector endpoint
        ConnectorEndpoint ce = new ConnectorEndpointBuilder(connector.getIdsHasDefaultEndpoint().getId())
                ._accessURL_(connector.getIdsHasDefaultEndpoint().getIdsAccessURL().getId())
                ._endpointDocumentation_(Util.asList(
                        metadata.getEndpointDocumentation()))
                .build();

        // Build the ids resource.
        //ToDO: created date?
        try {
            return new ResourceBuilder(
                    URI.create("https://w3id.org/idsa/autogen/resource/" + uuid))
                    ._contractOffer_(contracts)
                    ._description_(Util.asList(new TypedLiteral(metadata.getDescription(), "EN")))
                    ._keyword_(keywords)
                    ._language_(Util.asList(Language.EN))
                    ._modified_(getGregorianOf(new Date()))
                    ._publisher_(metadata.getOwner())
                    ._representation_(representations)
                    ._resourceEndpoint_(Util.asList(ce))
                    ._standardLicense_(metadata.getLicense())
                    ._title_(Util.asList(new TypedLiteral(metadata.getTitle(), "EN")))
                    ._version_(metadata.getVersion())
                    .build();
        } catch (ConstraintViolationException | NullPointerException exception) {
            // The build failed or the connector is null.
            throw new RuntimeException("Failed to build information model resource.", exception);
        }
    }

    /**
     * Converts a date to XMLGregorianCalendar format.
     *
     * @param date the date object.
     * @return the XMLGregorianCalendar object or null.
     */
    public XMLGregorianCalendar getGregorianOf(Date date) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (DatatypeConfigurationException exception) {
            // Rethrow but do not register in function header
            throw new RuntimeException(exception);
        }
    }
}

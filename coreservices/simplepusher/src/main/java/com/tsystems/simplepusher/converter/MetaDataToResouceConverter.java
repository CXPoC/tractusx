package com.tsystems.simplepusher.converter;

import com.tsystems.simplepusher.model.ids.IdsResourceMetadata;
import com.tsystems.simplepusher.model.ids.IdsResourceRepresentation;
import de.fraunhofer.iais.eis.*;
import de.fraunhofer.iais.eis.ids.jsonld.Serializer;
import de.fraunhofer.iais.eis.util.TypedLiteral;
import de.fraunhofer.iais.eis.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

/**
 * Original converting can be seen in {@link de.fraunhofer.isst.ids.framework.util.IDSUtils}
 */
@Component
@RequiredArgsConstructor
public class MetaDataToResouceConverter {

    /**
     * {@link BaseConnectorImpl} language "en" hardcoded here anyway.
     * Language.
     */
    private static final String LANGUAGE = "en";
    /**
     * Serializer of fraunhofer.
     */
    private final Serializer serializer;

    /**
     * Converts resourcemetadata to resource
     * The original code can be seen at {@link de.fraunhofer.isst.ids.framework.util.IDSUtils}
     *
     * @param metadata  metadata
     * @param connector connector information
     * @param uuid      UUID of resource
     * @return converter {@link Resource}
     */
    @SneakyThrows
    public Resource convert(IdsResourceMetadata metadata, Connector connector, UUID uuid) {
        // Get the list of keywords
        Map<UUID, IdsResourceRepresentation> mapMetaData = emptyIfNull(metadata.getRepresentations()).stream()
                .collect(Collectors.toMap(IdsResourceRepresentation::getUuid, Function.identity()));

        var keywords = new ArrayList<TypedLiteral>();
        if (metadata.getKeywords() != null) {
            for (var keyword : metadata.getKeywords()) {
                keywords.add(new TypedLiteral(keyword, LANGUAGE));
            }
        }

        // Get the list of representations
        var representations = new ArrayList<Representation>();
        if (metadata.getRepresentations() != null) {
            for (var representation : mapMetaData.values()) {
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

            }
        }

        // Get the list of contracts.
        var contracts = new ArrayList<ContractOffer>();
        if (metadata.getPolicy() != null) {
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
        }

        // Build the connector endpoint
        ConnectorEndpoint ce = new ConnectorEndpointBuilder(connector.getHasDefaultEndpoint().getId())
                ._accessURL_(connector.getHasDefaultEndpoint().getAccessURL())
                ._endpointDocumentation_(Util.asList(
                        metadata.getEndpointDocumentation()))
                .build();

        // Build the ids resource.
        return new ResourceBuilder(
                URI.create("https://w3id.org/idsa/autogen/resource/" + uuid))
                ._contractOffer_(contracts)
                ._description_(Util.asList(new TypedLiteral(metadata.getDescription(), LANGUAGE)))
                ._keyword_(keywords)
                ._language_(Util.asList(Language.EN))
                ._created_(getGregorianOf(Date.from(LocalDateTime.now().minusMonths(12).toInstant(ZoneOffset.UTC))))
                ._modified_(getGregorianOf(new Date()))
                ._publisher_(metadata.getOwner())
                ._representation_(representations)
                ._resourceEndpoint_(Util.asList(ce))
                ._standardLicense_(metadata.getLicense())
                ._title_(Util.asList(new TypedLiteral(metadata.getTitle(), LANGUAGE)))
                ._version_(metadata.getVersion())
                .build();
    }

    /**
     * Converts a date to XMLGregorianCalendar format.
     *
     * @param date the date object.
     * @return the XMLGregorianCalendar object or null.
     */
    @SneakyThrows
    public XMLGregorianCalendar getGregorianOf(Date date) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
    }
}

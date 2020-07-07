package Repository.XMLRepository;

import Domain.BaseEntity;
import Domain.Client;
import Domain.Validators.Validator;
import Domain.Validators.ValidatorException;
import Repository.InMemoryRepository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ClientXMLRepository extends InMemoryRepository<Long, Client> {
    private Map<Long,Client> entities;
    private String path;

    public ClientXMLRepository(Validator<Client> validator, String fileName) throws IOException, SAXException, ParserConfigurationException {
        super(validator);
        this.entities = new HashMap<>();
        this.path = fileName;

        entities = loadData();
    }

    private static String getTextFromTagName(Element parentElement, String tagName) {
        Node node = parentElement.getElementsByTagName(tagName).item(0);
        return node.getTextContent();
    }

    private static String getTextFromAttribute(Element parentElement, String attributeName) {
        return parentElement.getAttribute(attributeName);
    }

    private static Client createClientFromElement(Element clientElement){
        Client client = new Client();

        client.setId(Long.parseLong(getTextFromAttribute(clientElement,"id")));
        client.setFirstName(getTextFromTagName(clientElement, "fname"));
        client.setLastName(getTextFromTagName(clientElement, "lname"));
        client.setDateOfBirth(getTextFromTagName(clientElement, "dob"));
        client.setEmail(getTextFromTagName(clientElement, "email"));

        return client;
    }

    private static Map<Long,Client> loadData() throws ParserConfigurationException, IOException, SAXException {

        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse("./data/clients.xml");

        Element root = document.getDocumentElement();

        NodeList children = root.getChildNodes();
        return IntStream
                .range(0, children.getLength())
                .mapToObj(children::item)
                .filter(node -> node instanceof Element)
                .map(node -> createClientFromElement((Element) node))
                .collect(Collectors.toMap(Client::getId,Client::getEntity));


    }
    public static void saveClient(Client client) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse("./data/clients.xml");

        Element root = document.getDocumentElement();
        Node clientNode = clientToNode(client, document);
        root.appendChild(clientNode);

        Transformer transformer= TransformerFactory
                .newInstance()
                .newTransformer();
        transformer.transform(new DOMSource(document),
                new StreamResult(new File("./data/clients.xml")));
    }
    public static Node clientToNode(Client client, Document document){
        Element clientElement = document.createElement("client");
        clientElement.setAttribute("id", String.valueOf(client.getId()));

        Element fnameElement = document.createElement("fname");
        fnameElement.setTextContent(client.getFirstName());
        clientElement.appendChild(fnameElement);

        Element lnameElement = document.createElement("lname");
        lnameElement.setTextContent(client.getLastName());
        clientElement.appendChild(lnameElement);

        Element dobElement = document.createElement("dob");
        dobElement.setTextContent(client.getDateOfBirth());
        clientElement.appendChild(dobElement);

        Element emailElement = document.createElement("email");
        emailElement.setTextContent(client.getEmail());
        clientElement.appendChild(emailElement);
        return clientElement;
    }

    @Override
    public Optional<Client> findOne(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null!");
        }
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<Client> findAll() {
        return entities.values();
    }

    @Override
    public Optional<Client> save(Client entity) throws ValidatorException {
        if (entity == null) {
            throw new IllegalArgumentException("ID must not be null!");
        }
        validator.validate(entity);
        try {
            saveClient(entity);
        }catch (Exception exception) {
            throw new ValidatorException(exception.getMessage());
        }
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));    }

    @Override
    public Optional<Client> delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null!");
        }

        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse("./data/clients.xml");
            Element root = document.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node currentNode = nodeList.item(i);
                if (currentNode.getNodeType() == Node.ELEMENT_NODE && ((Element) currentNode).getAttribute("id").equals(id.toString())) {
                    root.removeChild(currentNode);
                }
            }

            Transformer transformer= TransformerFactory
                    .newInstance()
                    .newTransformer();
            transformer.transform(new DOMSource(document),
                    new StreamResult(new File("./data/clients.xml")));
        }catch (Exception exception) {
            throw new ValidatorException(exception.getMessage());
        }

        return Optional.ofNullable(entities.remove(id));
    }


    public void updateClient(Client client) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse("./data/clients.xml");

        Element root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE && ((Element) currentNode).getAttribute("id").equals(client.getId().toString())) {
                Node firstName = ((Element) currentNode).getElementsByTagName("fname").item(0);
                firstName.setTextContent(client.getFirstName());
                Node lastName = ((Element) currentNode).getElementsByTagName("lname").item(0);
                lastName.setTextContent(client.getLastName());
                Node dob = ((Element) currentNode).getElementsByTagName("dob").item(0);
                dob.setTextContent(client.getDateOfBirth());
                Node email = ((Element) currentNode).getElementsByTagName("email").item(0);
                email.setTextContent(client.getEmail());
            }
        }

        Transformer transformer= TransformerFactory
                .newInstance()
                .newTransformer();
        transformer.transform(new DOMSource(document),
                new StreamResult(new File("./data/clients.xml")));
    }

    @Override
    public Optional<Client> update(Client entity) throws ValidatorException{
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null!");
        }

        validator.validate(entity);
        try {
            updateClient(entity);
        }catch (Exception exception) {
            throw new ValidatorException(exception.getMessage());
        }
        return Optional.ofNullable(entities.computeIfPresent(entity.getId(), (k, v) -> entity));
    }
}

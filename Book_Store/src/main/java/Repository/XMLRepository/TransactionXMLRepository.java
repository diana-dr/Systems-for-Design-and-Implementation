package Repository.XMLRepository;

import Domain.BaseEntity;
import Domain.Transaction;
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

public class TransactionXMLRepository extends InMemoryRepository<Long, Transaction> {
    private Map<Long, Transaction> entities;
    private String path;

    public TransactionXMLRepository(Validator<Transaction> validator, String fileName) throws IOException, SAXException, ParserConfigurationException {
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

    private static Transaction createTransactionFromElement(Element transactionElement){
        Transaction transaction = new Transaction();

        transaction.setId(Long.parseLong(getTextFromAttribute(transactionElement,"id")));
        transaction.setBookID(Long.parseLong(getTextFromTagName(transactionElement, "book_id")));
        transaction.setClientID(Long.parseLong(getTextFromTagName(transactionElement, "client_id")));
        return transaction;
    }

    private static Map<Long,Transaction> loadData() throws ParserConfigurationException, IOException, SAXException {

        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse("./data/transactions.xml");

        Element root = document.getDocumentElement();

        NodeList children = root.getChildNodes();
        return IntStream
                .range(0, children.getLength())
                .mapToObj(children::item)
                .filter(node -> node instanceof Element)
                .map(node -> createTransactionFromElement((Element) node))
                .collect(Collectors.toMap(Transaction::getId,Transaction::getEntity));


    }
    public static void saveTransaction(Transaction client) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse("./data/transactions.xml");

        Element root = document.getDocumentElement();
        Node clientNode = transactionToNode(client, document);
        root.appendChild(clientNode);

        Transformer transformer= TransformerFactory
                .newInstance()
                .newTransformer();
        transformer.transform(new DOMSource(document),
                new StreamResult(new File("./data/transactions.xml")));
    }
    public static Node transactionToNode(Transaction transaction, Document document){
        Element transactionElement = document.createElement("transaction");
        transactionElement.setAttribute("id", String.valueOf(transaction.getId()));

        Element bookID = document.createElement("book_id");
        bookID.setTextContent(String.valueOf(transaction.getBookID()));
        transactionElement.appendChild(bookID);

        Element clientID = document.createElement("client_id");
        clientID.setTextContent(String.valueOf(transaction.getClientID()));
        transactionElement.appendChild(clientID);

        return transactionElement;
    }

    @Override
    public Optional<Transaction> findOne(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null!");
        }
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<Transaction> findAll() {
        return new HashSet<>(entities.values());
    }

    @Override
    public Optional<Transaction> save(Transaction entity) throws ValidatorException {
        if (entity == null) {
            throw new IllegalArgumentException("ID must not be null!");
        }
        validator.validate(entity);
        try {
            saveTransaction(entity);
        }catch (Exception exception) {
            throw new ValidatorException(exception.getMessage());
        }
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));    }

    @Override
    public Optional<Transaction> delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null!");
        }
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse("./data/transactions.xml");

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
                    new StreamResult(new File("./data/transactions.xml")));
        }catch (Exception exception) {
            throw new ValidatorException(exception.getMessage());
        }

        return Optional.ofNullable(entities.remove(id));
    }


    public void updateClient(Transaction transaction) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse("./data/transactions.xml");

        Element root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE && ((Element) currentNode).getAttribute("id").equals(transaction.getId().toString())) {
                Node book_id = ((Element) currentNode).getElementsByTagName("book_id").item(0);
                book_id.setTextContent(String.valueOf(transaction.getBookID()));
                Node client_id = ((Element) currentNode).getElementsByTagName("client_id").item(0);
                client_id.setTextContent(String.valueOf(transaction.getClientID()));
            }
        }

        Transformer transformer= TransformerFactory
                .newInstance()
                .newTransformer();
        transformer.transform(new DOMSource(document),
                new StreamResult(new File("./data/transactions.xml")));
    }

    @Override
    public Optional<Transaction> update(Transaction entity) throws ValidatorException {
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

package Repository.XMLRepository;

import Domain.BaseEntity;
import Domain.Book;

import Domain.Validators.Validator;
import Domain.Validators.ValidatorException;
import Repository.InMemoryRepository;
import org.w3c.dom.*;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class BookXMLRepository extends InMemoryRepository<Long, Book> {

    private Map<Long,Book> entities;
    private String path;

    public BookXMLRepository(Validator<Book> validator, String fileName) throws IOException, SAXException, ParserConfigurationException {
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


    private static Book createBookFromElement(Element bookElement){
        Book book = new Book();
        String category = bookElement.getAttribute("category");
        book.setCategory(category);

        book.setCategory(getTextFromAttribute(bookElement,"category"));
        book.setId(Long.parseLong(getTextFromAttribute(bookElement,"id")));
        book.setTitle(getTextFromTagName(bookElement, "title"));
        book.setAuthor(getTextFromTagName(bookElement, "author"));
        book.setReleaseDate(getTextFromTagName(bookElement, "releaseDate"));
        book.setPrice(Integer.parseInt(getTextFromTagName(bookElement, "price")));

        return book;
    }

    private static Map<Long,Book> loadData() throws ParserConfigurationException, IOException, SAXException {

        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse("./data/bookstore.xml");

        Element root = document.getDocumentElement();

        NodeList children = root.getChildNodes();
        return IntStream
                .range(0, children.getLength())
                .mapToObj(children::item)
                .filter(node -> node instanceof Element)
                .map(node -> createBookFromElement((Element) node))
                .collect(Collectors.toMap(Book::getId,Book::getEntity));
    }
    public static void saveBook(Book book) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse("./data/bookstore.xml");

        Element root = document.getDocumentElement();
        Node bookNode = bookToNode(book, document);
        root.appendChild(bookNode);

        Transformer transformer= TransformerFactory
                .newInstance()
                .newTransformer();
        transformer.transform(new DOMSource(document),
                new StreamResult(new File("./data/bookstore.xml")));
    }


    public static Node bookToNode(Book book, Document document){
        Element bookElement = document.createElement("book");
        bookElement.setAttribute("category", book.getCategory());
        bookElement.setAttribute("id", String.valueOf(book.getId()));

        Element titleElement = document.createElement("title");
        titleElement.setTextContent(book.getTitle());
        bookElement.appendChild(titleElement);

        Element authorElement = document.createElement("author");
        authorElement.setTextContent(book.getAuthor());
        bookElement.appendChild(authorElement);

        Element releaseDateElement = document.createElement("releaseDate");
        releaseDateElement.setTextContent(book.getReleaseDate());
        bookElement.appendChild(releaseDateElement);

        Element priceElement = document.createElement("price");
        priceElement.setTextContent(String.valueOf(book.getPrice()));
        bookElement.appendChild(priceElement);

        return bookElement;
    }

    public static void removeBook(Book book) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse("./data/bookstore.xml");

        Element root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE && ((Element) currentNode).getAttribute("id").equals(book.getId().toString())) {
                root.removeChild(currentNode);
            }
        }

        Transformer transformer= TransformerFactory
                .newInstance()
                .newTransformer();
        transformer.transform(new DOMSource(document),
                new StreamResult(new File("./data/bookstore.xml")));
    }

    public static void updateBook(Book book) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse("./data/bookstore.xml");

        Element root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE && ((Element) currentNode).getAttribute("id").equals(book.getId().toString())) {
                Node title = ((Element) currentNode).getElementsByTagName("title").item(0);
                title.setTextContent(book.getTitle());
                Node releaseDate = ((Element) currentNode).getElementsByTagName("releaseDate").item(0);
                releaseDate.setTextContent(book.getReleaseDate());
                Node author = ((Element) currentNode).getElementsByTagName("author").item(0);
                author.setTextContent(book.getAuthor());
                Node price = ((Element) currentNode).getElementsByTagName("price").item(0);
                price.setTextContent(String.valueOf(book.getPrice()));
            }
        }

        Transformer transformer= TransformerFactory
                .newInstance()
                .newTransformer();
        transformer.transform(new DOMSource(document),
                new StreamResult(new File("./data/bookstore.xml")));
    }

    @Override
    public Optional<Book> findOne(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null!");
        }
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<Book> findAll() {
        return entities.values();
    }

    @Override
    public Optional<Book> save(Book entity) throws ValidatorException {
        if (entity == null) {
            throw new IllegalArgumentException("ID must not be null!");
        }
        validator.validate(entity);
        try {
            saveBook(entity);
        }catch (Exception exception) {
            throw new ValidatorException(exception.getMessage());
        }
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));    }

    @Override
    public Optional<Book> delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null!");
        }
        Book book = entities.get(id);
        try {
            removeBook(book);
        }catch (Exception exception) {
            throw new ValidatorException(exception.getMessage());
        }
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<Book> update(Book entity) throws ValidatorException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null!");
        }
        validator.validate(entity);
        try {
            updateBook(entity);
        }catch (Exception exception) {
            throw new ValidatorException(exception.getMessage());
        }
        return Optional.ofNullable(entities.computeIfPresent(entity.getId(), (k, v) -> entity));
    }
}

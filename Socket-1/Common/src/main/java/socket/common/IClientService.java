package socket.common;

import java.util.concurrent.Future;

public interface IClientService extends IEntityService {
    Future<String> printSortedClients(String entity);
}

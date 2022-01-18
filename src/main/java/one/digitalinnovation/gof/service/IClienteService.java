package one.digitalinnovation.gof.service;

import one.digitalinnovation.gof.model.Cliente;

/**
 * Interface que define o padrão <b>Strategy</b>no domínio de cliente. Com
 * isso, se necessário, podemos ter multíplas implemtentações dessa mesma
 * interface.
 *
 * @author dgleyramos1
 * */
public interface IClienteService {
    Iterable<Cliente> buscarTodos();
    Cliente buscarPorId(Long id);
    void inserir(Cliente cliente);
    void atualizar(Long id, Cliente cliente);
    void deletar(Long id);
}

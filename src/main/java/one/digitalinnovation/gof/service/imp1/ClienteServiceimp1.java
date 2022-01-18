package one.digitalinnovation.gof.service.imp1;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.IClienteRepository;
import one.digitalinnovation.gof.model.IEnderecoRepository;
import one.digitalinnovation.gof.service.IClienteService;
import one.digitalinnovation.gof.service.IViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * Implementação da <b>Strategy</b> {@link IClienteService}, a qual pod ser
 * injetada pelo Spring (via {@link Autowired}). Com isso, como essa classe é um
 * {@link Service}, ela será tratada como um <b>singleton</b>.
 *
 * @author dgleyramos1
 *
 * */
@Service
public class ClienteServiceimp1 implements IClienteService {

    // Singleton: Injetar os componentes do Spring com @Autowired.
    @Autowired
    private IClienteRepository clienteRepository;
    @Autowired
    private IEnderecoRepository enderecoRepository;
    @Autowired
    private IViaCepService viaCepService;

    // Strategy: Implementar os métodos definidos na interface.
    // Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }

    @Override
    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);
    }


    @Override
    public void atualizar(Long id, Cliente cliente) {
        // Buscar Cliente por ID, caso exista.
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if (clienteBd.isPresent()) {
            salvarClienteComCep(cliente);
        }
    }

    @Override
    public void deletar(Long id) {
        // Deletar o Cliente por ID
        clienteRepository.deleteById(id);

    }

    private void salvarClienteComCep(Cliente cliente) {
        //Verificar se o Endereco do CLiente já existe (pelo CEP).
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            //Caso não exista, integrar com o ViaCep e persistir o retorno.
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        //Inserir Cliente, vínculando o nedereço (novo ou existente).
        clienteRepository.save(cliente);
    }
}












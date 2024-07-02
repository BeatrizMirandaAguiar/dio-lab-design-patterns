package one.digitalinnovation.gof.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;

public class ClienteServiceimpl  implements ClienteService{
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;
    public Iterable<Cliente> buscarTodos(){return clienteRepository.findAll();}
    public Cliente buscarPorId(Long id){
        Optional<Cliente> findById = clienteRepository.findById(id);
        if(!findById.isPresent()){
            return null;
        }else{return findById.get();}
    }
    public void inserir(Cliente cliente){salvarClienteComCep(cliente);}
    public void atualizar(Long id, Cliente cliente){
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if(clienteBd.isPresent()){salvarClienteComCep(cliente);}
    }
    public void deletar(Long id){
        clienteRepository.deleteById(id);
    }
    private void salvarClienteComCep(Cliente cliente) {
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(()->{
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return null;});
        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);
    }
}

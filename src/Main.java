import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

	private static final int OPTION_COMPANY_LIST_SALES = 1;
	private static final int OPTION_COMPANY_VIEW_PRODUCTS = 2;
	private static final int OPTION_CUSTOMER_MAKE_PURCHASE = 1;
	private static final int OPTION_CUSTOMER_VIEW_PURCHASES = 2;
	private static final int OPTION_LOGOUT = 0;

	public static void main(String[] args) {

		// SIMULANDO BANCO DE DADOS

		List<Produto> carrinho = new ArrayList<Produto>();
		List<Venda> vendas = new ArrayList<Venda>();

		Empresa empresa = new Empresa(2, "SafeWay Padaria", "30021423000159", 0.15, 0.0);
		Empresa empresa2 = new Empresa(1, "Level Varejo", "53239160000154", 0.05, 0.0);
		Empresa empresa3 = new Empresa(3, "SafeWay Restaurante", "41361511000116", 0.20, 0.0);

		Produto produto = new Produto(1, "Pão Frances", 5, 3.50, empresa);
		Produto produto2 = new Produto(2, "Coturno", 10, 400.0, empresa2);
		Produto produto3 = new Produto(3, "Jaqueta Jeans", 15, 150.0, empresa2);
		Produto produto4 = new Produto(4, "Calça Sarja", 15, 150.0, empresa2);
		Produto produto5 = new Produto(5, "Prato feito - Frango", 10, 25.0, empresa3);
		Produto produto6 = new Produto(6, "Prato feito - Carne", 10, 25.0, empresa3);
		Produto produto7 = new Produto(7, "Suco Natural", 30, 10.0, empresa3);
		Produto produto8 = new Produto(8, "Sonho", 5, 8.50, empresa);
		Produto produto9 = new Produto(9, "Croissant", 7, 6.50, empresa);
		Produto produto10 = new Produto(10, "Ché Gelado", 4, 5.50, empresa);

		Cliente cliente = new Cliente("07221134049", "Allan da Silva", "cliente", 20);
		Cliente cliente2 = new Cliente("72840700050", "Samuel da Silva", "cliente2", 24);

		Usuario usuario1 = new Usuario("admin", "1234", null, null);
		Usuario usuario2 = new Usuario("empresa", "1234", null, empresa);
		Usuario usuario3 = new Usuario("cliente", "1234", cliente, null);
		Usuario usuario4 = new Usuario("cliente2", "1234", cliente2, null);
		Usuario usuario5 = new Usuario("empresa2", "1234", null, empresa2);
		Usuario usuario6 = new Usuario("empresa3", "1234", null, empresa3);

		List<Usuario> usuarios = Arrays.asList(usuario1, usuario2, usuario3, usuario4, usuario5, usuario6);
		List<Cliente> clientes = Arrays.asList(cliente, cliente2);
		List<Empresa> empresas = Arrays.asList(empresa, empresa2, empresa3);
		List<Produto> produtos = Arrays.asList(produto, produto2, produto3, produto4, produto5, produto6, produto7,
				produto8, produto9, produto10);
		try {
			executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}

	public static int lerOpcaoNumericaValida(Scanner sc, int minOpcao, int maxOpcao) {
		while (true) {
			try {
				int escolha = Integer.parseInt(sc.next());
				if (escolha >= minOpcao && escolha <= maxOpcao) {
					return escolha;
				} else {
					System.out.println("Opção inválida. Digite novamente:");
				}
			} catch (NumberFormatException e) {
				System.out.println("Número inválido. Digite novamente:");
			}
		}
	}

	public static void executar(List<Usuario> usuarios, List<Cliente> clientes, List<Empresa> empresas,
			List<Produto> produtos, List<Produto> carrinho, List<Venda> vendas) {
		Scanner sc = new Scanner(System.in);
		try {
			System.out.println("Bem-vindo ao Sistema de Vendas!");
			System.out.println("Entre com seu usuário e senha:");

			System.out.print("Usuário: ");
			String username = sc.next();
			System.out.print("Senha: ");
			String senha = sc.next();

			List<Usuario> usuariosSearch = usuarios.stream().filter(x -> x.getUsername().equals(username))
					.collect(Collectors.toList());
			if (usuariosSearch.size() > 0) {
				Usuario usuarioLogado = usuariosSearch.get(0);
				if ((usuarioLogado.getSenha().equals(senha))) {

					System.out.println("Escolha uma opção para iniciar");
					if (usuarioLogado.IsEmpresa()) {
						System.out.println("1 - Listar vendas");
						System.out.println("2 - Ver produtos");
						System.out.println("0 - Deslogar");

						int minOpcao = usuarioLogado.IsEmpresa() ? OPTION_COMPANY_LIST_SALES
								: OPTION_CUSTOMER_MAKE_PURCHASE;
						int maxOpcao = usuarioLogado.IsEmpresa() ? OPTION_COMPANY_VIEW_PRODUCTS
								: OPTION_CUSTOMER_VIEW_PURCHASES;

						int opcaoEscolhida = lerOpcaoNumericaValida(sc, minOpcao, maxOpcao);

						switch (opcaoEscolhida) {
							case OPTION_COMPANY_LIST_SALES: {
								System.out.println();
								System.out.println("************************************************************");
								System.out.println("VENDAS EFETUADAS");
								vendas.stream().forEach(venda -> {
									if (venda.getEmpresa().getId().equals(usuarioLogado.getEmpresa().getId())) {
										System.out.println(
												"************************************************************");
										System.out.println("Venda de código: " + venda.getCódigo() + " no CPF "
												+ venda.getCliente().getCpf() + ": ");
										venda.getItens().stream().forEach(x -> {
											System.out
													.println(x.getId() + " - " + x.getNome() + "    R$" + x.getPreco());
										});
										System.out.println("Total Venda: R$" + venda.getValor());
										System.out.println("Total Taxa a ser paga: R$" + venda.getComissaoSistema());
										System.out.println("Total Líquido  para empresa"
												+ (venda.getValor() - venda.getComissaoSistema()));
										System.out.println(
												"************************************************************");
									}

								});
								System.out.println("Saldo Empresa: " + usuarioLogado.getEmpresa().getSaldo());
								System.out.println("************************************************************");

								executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
								break;
							}

							case OPTION_COMPANY_VIEW_PRODUCTS: {
								System.out.println();
								System.out.println("************************************************************");
								System.out.println("MEUS PRODUTOS");
								produtos.stream().forEach(produto -> {
									if (produto.getEmpresa().getId().equals(usuarioLogado.getEmpresa().getId())) {
										System.out.println(
												"************************************************************");
										System.out.println("Código: " + produto.getId());
										System.out.println("Produto: " + produto.getNome());
										System.out.println("Quantidade em estoque: " + produto.getQuantidade());
										System.out.println("Valor: R$" + produto.getPreco());
										System.out.println(
												"************************************************************");
									}

								});
								System.out.println("Saldo Empresa: " + usuarioLogado.getEmpresa().getSaldo());
								System.out.println("************************************************************");

								executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
								break;
							}

							case OPTION_LOGOUT: {
								executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
								break;
							}

						}

					} else {
						System.out.println("1 - Relizar Compras");
						System.out.println("2 - Ver Compras");
						System.out.println("0 - Deslogar");
						int minOpcao = usuarioLogado.IsEmpresa() ? OPTION_COMPANY_LIST_SALES
								: OPTION_CUSTOMER_MAKE_PURCHASE;
						int maxOpcao = usuarioLogado.IsEmpresa() ? OPTION_COMPANY_VIEW_PRODUCTS
								: OPTION_CUSTOMER_VIEW_PURCHASES;

						int opcaoEscolhida = lerOpcaoNumericaValida(sc, minOpcao, maxOpcao);
						switch (opcaoEscolhida) {
							case OPTION_CUSTOMER_MAKE_PURCHASE: {
								System.out.println("Para realizar uma compra, escolha a empresa onde deseja comprar: ");
								empresas.stream().forEach(x -> {
									System.out.println(x.getId() + " - " + x.getNome());
								});
								int escolhaEmpresa = lerOpcaoNumericaValida(sc, 1, empresas.size());
								Integer escolhaProduto = -1;
								do {
									System.out.println("Escolha os seus produtos: ");
									produtos.stream().forEach(x -> {
										if (x.getEmpresa().getId().equals(escolhaEmpresa)) {
											System.out.println(x.getId() + " - " + x.getNome());
										}
									});
									System.out.println("0 - Finalizar compra");
									escolhaProduto = sc.nextInt();
									for (Produto produtoSearch : produtos) {
										if (produtoSearch.getId().equals(escolhaProduto))
											carrinho.add(produtoSearch);
									}
								} while (escolhaProduto != 0);
								System.out.println("************************************************************");
								System.out.println("Resumo da compra: ");
								carrinho.stream().forEach(x -> {
									if (x.getEmpresa().getId().equals(escolhaEmpresa)) {
										System.out.println(x.getId() + " - " + x.getNome() + "    R$" + x.getPreco());
									}
								});
								Empresa empresaEscolhida = empresas.stream()
										.filter(x -> x.getId().equals(escolhaEmpresa))
										.collect(Collectors.toList()).get(0);
								Cliente clienteLogado = clientes.stream()
										.filter(x -> x.getUsername().equals(usuarioLogado.getUsername()))
										.collect(Collectors.toList()).get(0);
								Venda venda = criarVenda(carrinho, empresaEscolhida, clienteLogado, vendas);
								System.out.println("Total: R$" + venda.getValor());
								System.out.println("************************************************************");
								carrinho.clear();
								executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
								break;
							}

							case OPTION_CUSTOMER_VIEW_PURCHASES: {
								System.out.println();
								System.out.println("************************************************************");
								System.out.println("COMPRAS EFETUADAS");
								vendas.stream().forEach(venda -> {
									if (venda.getCliente().getUsername().equals(usuarioLogado.getUsername())) {
										System.out.println(
												"************************************************************");
										System.out.println("Compra de código: " + venda.getCódigo() + " na empresa "
												+ venda.getEmpresa().getNome() + ": ");
										venda.getItens().stream().forEach(x -> {
											System.out
													.println(x.getId() + " - " + x.getNome() + "    R$" + x.getPreco());
										});
										System.out.println("Total: R$" + venda.getValor());
										System.out.println(
												"************************************************************");
									}

								});
								executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
								break;
							}

							case OPTION_LOGOUT: {
								executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
								break;
							}
							default:
								System.out.println("Opção inválida.");

						}

					}

				} else
					System.out.println("Senha incorreta");
			} else {
				System.out.println("Usuário não encontrado");
			}
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		} finally {
			sc.close();
		}
	}

	public static Venda criarVenda(List<Produto> carrinho, Empresa empresa, Cliente cliente, List<Venda> vendas) {
		try {
			Double total = carrinho.stream().mapToDouble(Produto::getPreco).sum();
			Double comissaoSistema = total * empresa.getTaxa();
			int idVenda = vendas.isEmpty() ? 1 : vendas.get(vendas.size() - 1).getCódigo() + 1;
			Venda venda = new Venda(idVenda, new ArrayList<>(carrinho), total, comissaoSistema, empresa, cliente);
			empresa.setSaldo(empresa.getSaldo() + total - comissaoSistema);
			vendas.add(venda);
			return venda;
		} catch (Exception e) {
			System.out.println("An error occurred while creating a sale: " + e.getMessage());
			return null;
		}
	}
}
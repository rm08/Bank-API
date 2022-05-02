# Bank-API
Fazer uma API REST para atender operações básicas de uma conta bancária

EXIGÊNCIA PARA O EXERCÍCIO
▪ Não é permitido usar Hibernate, JPA ou qualquer mecanismo objeto-relacional. É obrigatório 
fazer os SQLs na “raça”.
▪ Deve-se criar um projeto com Spring Boot do zero.
▪ É preciso seguir o padrão de mercado RESTful para nomenclatura dos endpoints.
▪ Exercite os conceitos de O.O. para criar a classe da conta corrente.
▪ Segregue as regras de negócio em classes independentes.

DESCRIÇÃO
Criar uma API REST em Spring Boot para que seja possível executar as seguintes operações:
• Criar uma conta corrente
O endpoint deverá receber como parâmetro o número da agência (int), um correntista (objeto 
com nome completo, nascimento e CPF), o limite (Big Decimal) e o saldo inicial (BigDecimal, 
com valor > 0) e deverá retornar uma conta corrente. O número da conta corrente é gerado pelo 
serviço e é um valor sequencial, dependendo da agência. Isso implica que cada agência tem a 
sua própria numeração de conta corrente e que podem existir contas de mesmo número desde 
que sejam de agências diferentes. Caso o limite de saque fornecido seja maior que 0, a conta 
será tratada como especial e pode ficar negativa até o limite estabelecido. Uma conta com limite 
zero é considerada normal e não pode ficar negativa. O código de retorno HTTP deve ser 201 
quando criar a conta com sucesso. Se a agência fornecida não existir, deverá retornar 404.

• Fazer uma consulta de saldo
O endpoint deverá receber o número da agência e o número da conta (int) e deve retornar o 
saldo (BigDecimal) em conta corrente. O código de retorno HTTP deve ser 200. Caso a conta 
não exista, deve retornar 404.

• Fazer um depósito
O endpoint deverá receber os números de agência e conta, bem como o valor (BigDecimal) a 
ser depositado e deve retornar o novo saldo da conta corrente (BigDecimal). O código de retorno 
HTTP deve ser 200 em caso de sucesso. Caso o valor solicitado seja menor ou igual a zero, 
deve retornar HTTP 403. Caso a conta não exista, deve retornar 404.

• Fazer um saque
O endpoint deverá receber os números de agência e conta, bem como o valor (BigDecimal) a 
ser sacado e deve retornar o novo saldo da conta corrente (BigDecimal). O código de retorno 
HTTP deve ser 200 em caso de sucesso. Caso o valor solicitado seja menor ou igual a zero, 
deve retornar HTTP 403. Caso a conta não exista, deve retornar 404.

• Fazer uma transferência
O endpoint deverá receber os números de agência e conta da origem do dinheiro, assim como 
os números de agência e conta do destino, e o valor (BigDecimal) a ser transferido, e deve 
retornar o novo saldo da conta corrente de origem (BigDecimal). Caso o valor transferido seja 
menor ou igual a zero, deve retornar HTTP 403. Caso qualquer das contas não exista, deve 
retornar 404. O código de retorno HTTP deve ser 200 em caso de sucesso.

• Consultar dados do correntista
O endpoint deverá receber um CPF e retornar os dados pessoais do correntista, bem como uma 
lista de todas as contas que ele possui (apenas número da agência, número da conta e uma 
indicação se a conta é comum ou especial). Caso o CPF não exista, deve retornar 404. O código 
de retorno HTTP deve ser 200 em caso de sucesso.

• Desativar uma conta corrente
O endpoint deverá receber como parâmetro o número da agência, um número de conta e deverá 
retornar a conta corrente inativa. O código de retorno HTTP deve ser 200 quando a conta ficar 
inativa. Caso a conta não exista, deve retornar HTTP 403. Se a conta já estava inativa, retornar 
HTTP 304.

REGRAS DE NEGÓCIO
1. Nenhuma operação de saldo, depósito, saque ou transferência pode envolver uma conta inativa. 
Se isso for feito, a operação deve retornar HTTP 400.

2. Assuma que a tabela Agencia já está populada. Para efeito do exercício, dê uma carga de dados 
nessa tabela, já que ela não será alimentada pela API.

3. Uma conta comum não pode ficar negativa.

4. Uma conta especial pode ficar negativa, apenas até o limite estabelecido. Exemplo: Se o limite 
é 200, o saldo pode ficar até -200.

5. Se uma conta ficar abaixo do seu limite (na comum, o limite é zero), a operação deve ser barrada 
e retornar HTTP 403.

ESTRUTURA DAS TABELAS
Correntista
ID int autoincremento (PK)
Nome varchar(200) not null
CPF varchar(11) not null (AK – alternate key)
Nascimento date not null

Agencia
ID int autoincremento (PK) – código usado nos serviços
Nome varchar(200) not null
Endereco varchar(500) not null

ContaCorrente
ID int autoincremento (PK) – código usado nos serviços
ID_Correntista int (FK Correntista)
ID_Agencia int (FK Agencia)
Limite decimal (18,2)
Saldo decimal (18,2)
Ativa char(1) com check constraint aceitando apenas ‘T’ ou ‘F’ para true ou false

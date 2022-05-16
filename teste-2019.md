# Programação Concorrente 2019

## Parte I

1. Num algoritmo de escalonamento baseado em recursos uma receção constante de processos de baixo
custo pode causar *starvation* num processo que precise de uma grande quantidade de recursos.
Para resolver esta situação podemos ter em conta a idade do processo e pesar a idade do processo e o
seu custo para passar à frente na execução.

2. Não sei.

3. Em Java o programa tem de funcionar através de memória partilhada enquanto que em erlang é por um
sistema de mensagens...

## Parte II

Ver se o this.ref.start() funciona corretamente

```Java
interface Jogo {
	Partida participa();
}
interface Partida {
	String adivinha(int n);
}

class Game implements Jogo {
	private int num;
	private Partida ref;
	private final int max = 4;

	Game() {
		this.num = 0;
		this.ref = null;
	}

	public synchronized Partida participa() {
		if (this.num == 0) {
			this.ref.start();
			this.ref = new Match();
		}
		this.num = (this.num+1) % this.max;
		return this.ref;
	}
}

class Match implements Partida {
	private int tentativas;
	private long inicio;
	private int resposta;
	private boolean acabou
	private final max = 100;
	private final dur = 60*1000;

	Match() {
		this.tentativas = 0;
		this.resposta = new Random.nextInt(this.max) + 1;
		this.acabou = false;
	}

	public void start() {
		this.inicio = System.currentTimeMillis();
	}

	public synchronized String advinha(int n) {
		this.tentativas++;
		if ((System.currentTimeMillis() - this.inicio) > this.dur) {
			return "TEMPO";
		} else if (this.tentativas > 100) {
			return "TENTATIVAS";
		} else if (acabou) {
			return "PERDEU";
		} else {
			if (n == this.resposta) {
				return "GANHOU";
			} else if (n > this.resposta) {
				return "MENOR";
			} else {
				return "MAIOR";
			}
		}
	}
}
```

class InvalidAccount extends Exception {}
class NotEnoughFunds extends Exception {}

interface BankI {
    void deposit(int id, int val) throws InvalidAccount, NotEnoughFunds;
    void withdraw(int id, int val) throws InvalidAccount, NotEnoughFunds;
    int totalBalance(int accounts[]) throws InvalidAccount;
    void transfer(int from, int to, int amount) throws InvalidAccount, NotEnoughFunds;
}

class Account {
    private int balance;

    Account() {
        this.balance = 0;
    }

    public int getBalance() {
        return this.balance;
    }

    public void deposit(int val) throws NotEnoughFunds {
        if (val < 0)
            throw new NotEnoughFunds();
        
        this.balance += val;
    }

    public void withdraw(int val) throws NotEnoughFunds {
        if (val < 0 || this.balance < val)
            throw new NotEnoughFunds();
        
        this.balance -= val;
    }
}

class Bank implements BankI {
    private Account[] accounts;
    private int maxAccounts;

    Bank(int n) {
        this.maxAccounts = n;
        this.accounts = new Account[n];
        for (int i = 0; i < n; i++)
            this.accounts[i] = new Account();
    }

    public void deposit(int id, int val) throws InvalidAccount, NotEnoughFunds {
        if (id < 0 || id >= maxAccounts)
            throw new InvalidAccount();
        
        this.accounts[id].deposit(val);
    }

    public void withdraw(int id, int val) throws InvalidAccount, NotEnoughFunds {
        if (id < 0 || id >= maxAccounts)
            throw new InvalidAccount();
        
        this.accounts[id].withdraw(val);
    }

    public int totalBalance(int accounts[]) throws InvalidAccount {
        int total = 0;

        for (int id : accounts) {
            if (id < 0 || id >= maxAccounts)
                throw new InvalidAccount();
            
            total += this.accounts[id].getBalance();
        }

        return total;
    }

    public int getAccountBalance(int id) throws InvalidAccount {
        if (id < 0 || id >= maxAccounts)
            throw new InvalidAccount();

        return this.accounts[id].getBalance();
    }

    public void transfer(int from, int to, int amount) throws InvalidAccount, NotEnoughFunds {
        if (from < 0 || from >= maxAccounts || to < 0 || to >= maxAccounts)
            throw new InvalidAccount();
        
        this.accounts[from].withdraw(amount);
        this.accounts[to].deposit(amount);
    }
}

class Depositor extends Thread {
    private Bank bank;
    private int iterator;

    Depositor(Bank bank, int iterator) {
        this.bank = bank;
        this.iterator = iterator;
    }

    public void run() {
        try {
            for (int i = 0; i < iterator; i++)
                this.bank.deposit(i, 100*i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException, InvalidAccount, NotEnoughFunds {
        final int NC = Integer.parseInt(args[0]);
        final int N = Integer.parseInt(args[1]);
        final int I = Integer.parseInt(args[2]);

        Bank bank = new Bank(NC);
        Thread[] arr = new Depositor[N];

        for (int i = 0; i < N; i++)
            arr[i] = new Depositor(bank, I);
        for (int i = 0; i < N; i++)
            arr[i].start();
        for (int i = 0; i < N; i++)
            arr[i].join();

        for (int i = 0; i < N; i++)
            System.out.println(
                bank.getAccountBalance(i)
            );

        bank.transfer(3, 0, 150);

        for (int i = 0; i < N; i++)
            System.out.println(
                bank.getAccountBalance(i)
            );
    }
}

import java.util.HashMap;

class InvalidAccount extends Exception {}
class NotEnoughFunds extends Exception {}

interface BankI {
    int createAccount(int initialBalance);
    int closeAccount(int id) throws InvalidAccount;
    void deposit(int id, int val) throws InvalidAccount;
    void withdraw(int id, int val) throws InvalidAccount, NotEnoughFunds;
    void transfer(int from, int to, int amount) throws InvalidAccount, NotEnoughFunds;
    int totalBalance(int accounts[]) throws InvalidAccount;
}

class Account {
    private int balance;

    Account() {
        this.balance = 0;
    }

    Account(int n) {
        this.balance = n;
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
    private HashMap<Integer, Account> accounts;
    private int accCounter;
    Lock lock = new ReentrantLock();

    Bank() {
        this.accCounter = 0;
        this.accounts = new HashMap<Integer, Account>();
    }

    int createAccount(int initialBalance) {
        int accNum;
        this.lock.lock();

        try {
            accNum = this.accCounter++;
            this.accounts.put(accNum, new Account(initialBalance));
        } finally {
            this.lock.unlock();
        }

        return accNum;
    }

    int closeAccount(int id) throws InvalidAccount {
        int bal;
        this.lock.lock();

        try {
            if (!this.accounts.containsKey(id))
                throw new InvalidAccount();
            bal = this.accounts.remove(id).getBalance();
        } finally {
            this.lock.unlock();
        }

        return bal;
    }

    public void deposit(int id, int val) throws InvalidAccount, NotEnoughFunds {
        this.lock.lock();

        try {
            if (!this.accounts.containsKey(id))
                throw new InvalidAccount();
            this.accounts.get(id).deposit(val);
        } finally {
            this.lock.unlock();
        }
    }

    public void withdraw(int id, int val) throws InvalidAccount, NotEnoughFunds {
        this.lock.lock();

        try {
            if (!this.accounts.containsKey(id))
                throw new InvalidAccount();
            this.accounts.get(id).withdraw(val);
        } finally {
            this.lock.unlock();
        }
    }

    public void transfer(int from, int to, int amount) throws InvalidAccount, NotEnoughFunds {
        this.lock.lock();

        try {
            if (!this.accounts.containsKey(from) || !this.accounts.containsKey(from))
                throw new InvalidAccount();

            this.accounts.get(from).withdraw(amount);
            this.accounts.get(to).deposit(amount);
        } finally {
            this.lock.unlock();
        }
    }

    public int totalBalance(int accounts[]) throws InvalidAccount {
        int total = 0;

        for (int id : accounts) {
            if (!this.accounts.containsKey(id))
                throw new InvalidAccount();

            total += this.accounts.get(id).getBalance();
        }

        return total;
    }

    public int getAccountBalance(int id) throws InvalidAccount {
        if (!this.accounts.containsKey(id))
            throw new InvalidAccount();

        return this.accounts.get(id).getBalance();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<Integer, Account> entry : this.accounts.entrySet())
            sb.append(entry.getKey()).append(", ").append(entry.getValue()).append(";\n");

        return sb.toString();
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException, InvalidAccount, NotEnoughFunds {
        try {
            int n = 10;
            int accounts[] = new int[n];
            Bank b = new Bank();

            for (int i = 0; i < n; i++)
                accounts[i] = b.createAccount(100 * (i+1));

            System.out.println(b.toString());
            System.out.println("Closed account 5 with $" + b.closeAccount(5) + ".");

            b.transfer(9, 0, 300);
            int newList[] = {0,1,2,3,4,6,7,8,9};
            System.out.println(b.totalBalance(newList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

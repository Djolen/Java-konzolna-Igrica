package igra;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

abstract class Heroj{
    protected int HP;
    protected boolean isDead = false;
    protected String ime;

    public Heroj(int HP, String ime) {
        this.HP = HP;
        this.ime = ime;
    }

    public boolean isDead() { return isDead; }

    public int getHP() {
        return HP;
    }

    public void ubiIgraca(){
        isDead = true;
    }

    public void primiStetu(int steta){
        HP = HP - steta;
    }

    public String getIme() {
        return ime;
    }
}

class Carobnjak extends Heroj{

    private static int hpCarobnjak = 100;

    public Carobnjak(String ime) {
        super(hpCarobnjak, ime);
    }
}

class Macevalac extends Heroj{

    private static int hpMacevalac = 150;
    private String[] inventory = {null, null};
    private String equippedWeapon;

    public Macevalac(String ime) {
        super(hpMacevalac, ime);
    }

    public void dodeliOruzije(String oruzije, int invNum){
        if(inventory[invNum] == null){
            inventory[invNum] = oruzije;
            System.out.println("Dodeljeno oruzije");
        }else{
            System.out.println("Slot za oruzije je zauzet");
        }
    }

    public  void baciOruzije(int invNum){
        if(!(inventory[invNum] == null)){
            inventory[invNum] = null;
            System.out.println("Orzije baceno");
        }
        else{
            System.out.println("Nema oruzija u datom slotu");
        }
    }

    public String getInventory(int br) {
        return inventory[br];
    }

    public void equipWeapon(String weapon){
        this.equippedWeapon = weapon;
    }

    public String getEquippedWeapon() {
        return equippedWeapon;
    }

}

interface EnemyTakeDamage{
    void takeDamage(int damage);
    void setDeadEnemy();
}

class Zmaj implements EnemyTakeDamage{

    private static  Zmaj zmaj;
    private  int hpZmaj;
    private boolean isDead;

    protected Zmaj(){
        hpZmaj = 100;
        isDead = false;
    }

    public static Zmaj getZmaj() {
        if(zmaj == null){
            zmaj = new Zmaj();
        }
        return zmaj;
    }

    public int getHpZmaj() {
        return hpZmaj;
    }

    @Override
    public void takeDamage(int damage) {
        hpZmaj =  hpZmaj - damage;
    }

    @Override
    public void setDeadEnemy() {
        isDead = true;
    }

    public boolean isDead() {
        return isDead;
    }
}

class Pauk implements EnemyTakeDamage{

    private static  Pauk pauk;
    private int hpPauk;
    private boolean isDead;

    protected Pauk(){
        hpPauk = 75;
        isDead = false;
    }

    public static Pauk getPauk() {
        if(pauk == null){
            pauk = new Pauk();
        }
        return pauk;
    }

    public int getHpPauk() {
        return hpPauk;
    }

    @Override
    public void takeDamage(int damage) {
        hpPauk = hpPauk - damage;
    }

    @Override
    public void setDeadEnemy() {
        isDead = true;
    }

    public boolean isDead() {
        return isDead;
    }
}

class CountDead {
    private static int CountDeadPlayers = 0;
    private static int CountDeadEnemies = 0;

    public int getCountDeadPlayers() {
        return CountDeadPlayers;
    }

    public int getCountDeadEnemies() {
        return CountDeadEnemies;
    }

    public void CountDeadPlayersPlus(){
        CountDeadPlayers++;
    }

    public void CountDeadenemiesPlus(){
        CountDeadEnemies++;
    }
}

class NemaMestaException extends Exception {

    public NemaMestaException(String message){
        super(message);
    }
}

public class Test {

    static CountDead cd = new CountDead();

    public static void borbaSaZmajem(Macevalac macevalac, BufferedWriter out, Zmaj zmaj) throws IOException {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 100 + 1);
        int napadMacem = 10;
        int napadKopljem = 15;
        if (randomNum < 50) {
            System.out.println("Uspesno ste napali!");
            if (macevalac.getEquippedWeapon().equalsIgnoreCase("mac")) {
                zmaj.takeDamage(napadMacem);
                {
                    String zaUnos = ("[" + macevalac.getIme() + "] je napao [Zmaja] pomocu [Maca]\n");
                    System.out.println(zaUnos);
                    out.write(zaUnos);
                    out.close();
                    if (zmaj.getHpZmaj() > 0) {
                        System.out.println("Zmaju je ostalo: " + zmaj.getHpZmaj() + " HP-a");
                    }
                }
                if (zmaj.getHpZmaj() <= 0) {
                    System.out.println("Uspesno ste pobedili zmaja! ");
                    cd.CountDeadenemiesPlus();
                    zmaj.setDeadEnemy();
                }
            }
            if (macevalac.getEquippedWeapon().equalsIgnoreCase("koplje")) {
                zmaj.takeDamage(napadKopljem);
                {
                    String zaUnos = ("[" + macevalac.getIme() + "]je napao [Zmaja] pomocu [Koplja]\n");
                    System.out.println(zaUnos);
                    out.write(zaUnos);
                    out.close();
                    if (zmaj.getHpZmaj() > 0) {
                        System.out.println("Zmaju je ostalo: " + zmaj.getHpZmaj() + " HP-a");
                    }
                }
                if (zmaj.getHpZmaj() <= 0) {
                    System.out.println("Uspesno ste pobedili zmaja! ");
                    cd.CountDeadenemiesPlus();
                    zmaj.setDeadEnemy();
                }
            }
        } else {
            System.out.println("Primili ste udarac");
            int randomNapad = ThreadLocalRandom.current().nextInt(0, 1 + 1);
            if (randomNapad == 0) {
                macevalac.primiStetu(5);
                {
                    String zaUnos = ("[Zmaj] je napao [" + macevalac.getIme() + "] pomoću [Obicnog Napada]\n");
                    System.out.println(zaUnos);
                    out.write(zaUnos);
                    out.close();
                }
                if (macevalac.getHP() < 0) {
                    System.out.println( macevalac.getIme() + " Jedan je pobedjen! ");
                    macevalac.ubiIgraca();
                    cd.CountDeadPlayersPlus();
                }
                System.out.println( macevalac.getIme() + " je ostalo: " + macevalac.getHP() + " HP-a");
            } else {
                macevalac.primiStetu(20);
                {
                    String zaUnos = ("[Zmaj] je napao " + macevalac.getIme()  + " pomoću [bljuvanja vatre]\n");
                    System.out.println(zaUnos);
                    out.write(zaUnos);
                    out.close();
                }
                if (macevalac.getHP() < 0) {
                    System.out.println( macevalac.getIme() + " je pobedjen! ");
                    macevalac.ubiIgraca();
                    cd.CountDeadPlayersPlus();
                }
                System.out.println("["+ macevalac.getIme() + "] je ostalo: " + macevalac.getHP() + " HP-a");
            }
        }
    }

    public static void borbaSaPaukom(Macevalac macevalac, BufferedWriter out,  Pauk pauk) throws IOException {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 100 + 1);
        int napadMacem = 10;
        int napadKopljem = 15;
        if (randomNum < 50) {
            System.out.println("Uspesno ste napali!");
            if (macevalac.getEquippedWeapon().equalsIgnoreCase("mac")) {
                pauk.takeDamage(napadMacem);
                {
                    String zaUnos = ("[" + macevalac.getIme() + "] je napao [Pauka] pomocu [Maca]\n");
                    System.out.println(zaUnos);
                    out.write(zaUnos);
                    out.close();
                    if (pauk.getHpPauk() > 0) {
                        System.out.println("Pauku je ostalo: " + pauk.getHpPauk() + " HP-a");
                    }
                }
                if (pauk.getHpPauk() <= 0) {
                    System.out.println("Uspesno ste pobedili pauka! ");
                    cd.CountDeadenemiesPlus();
                    pauk.setDeadEnemy();
                }
            }
            if (macevalac.getEquippedWeapon().equalsIgnoreCase("koplje")) {
                pauk.takeDamage(napadKopljem);
                {
                    String zaUnos = ("[" + macevalac.getIme() + "] je napao [Pauka] pomocu [Koplja]\n");
                    System.out.println(zaUnos);
                    out.write(zaUnos);
                    out.close();
                    if (pauk.getHpPauk() > 0) {
                        System.out.println("Pauku je ostalo: " + pauk.getHpPauk() + " HP-a");
                    }
                }
                if (pauk.getHpPauk() <= 0) {
                    System.out.println("Uspesno ste pobedili pauka!");
                    cd.CountDeadenemiesPlus();
                    pauk.setDeadEnemy();
                }
            }
        } else {
            System.out.println("Primili ste udarac");
            int randomNapad = ThreadLocalRandom.current().nextInt(0, 1 + 1);
            if (randomNapad == 0) {
                macevalac.primiStetu(5);
                {
                    String zaUnos = ("[Pauk] je napao ["+ macevalac.getIme() +"] pomoću [Obicnog Napada]\n");
                    System.out.println(zaUnos);
                    out.write(zaUnos);
                    out.close();
                }
                if (macevalac.getHP() < 0) {
                    System.out.println("["+ macevalac.getIme()+"] Jedan je pobedjen! ");
                    macevalac.ubiIgraca();
                    cd.CountDeadPlayersPlus();
                }
                System.out.println("["+macevalac.getIme() +"] je ostalo: " + macevalac.getHP() + " HP-a");
            } else {
                macevalac.primiStetu(20);
                {
                    String zaUnos = ("[Pauk] je napao ["+ macevalac.getIme() +"] pomoću [Specijalne mreze]\n");
                    System.out.println(zaUnos);
                    out.write(zaUnos);
                    out.close();
                }
                if (macevalac.getHP() < 0) {
                    System.out.println("["+ macevalac.getIme()+"] Jedan je pobedjen! ");
                    macevalac.ubiIgraca();
                    cd.CountDeadPlayersPlus();
                }
                System.out.println("["+macevalac.getIme() +"] je ostalo: " + macevalac.getHP() + " HP-a");
            }
        }
    }

    public static void CarobnjakborbaSaZmajem(Carobnjak carobnjak, BufferedWriter out, Zmaj zmaj) throws IOException {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 100 + 1);
        if (randomNum < 50) {
            String napad = "Magija";
            int CarobnjakDamage = 80;
            //System.out.println("Uspesno ste napali!");
            zmaj.takeDamage(CarobnjakDamage);
            {
                String zaUnos = ("["+ carobnjak.getIme()+"] je napao [Zmaj] pomocu[" + napad +"]\n");
                System.out.println(zaUnos);
                out.write(zaUnos);
                out.close();
                if (zmaj.getHpZmaj() > 0) {
                    System.out.println("[Zmaju] je ostalo: " + zmaj.getHpZmaj() + " HP-a");
                }
            }
            if (zmaj.getHpZmaj() <= 0) {
                System.out.println("Uspesno ste pobedili zmaja! ");
                cd.CountDeadenemiesPlus();
                zmaj.setDeadEnemy();
            }

        } else {
            //System.out.println("Primili ste udarac");
            int randomNapad = ThreadLocalRandom.current().nextInt(0, 1 + 1);
            if (randomNapad == 0) {
                String napad = "Obican Napad";
                carobnjak.primiStetu(5);
                {
                    String zaUnos = ("[Zmaj] je napao ["+ carobnjak.getIme() +"] pomocu[" + napad +"]\n");
                    System.out.println(zaUnos);
                    out.write(zaUnos);
                    out.close();
                }
                if (carobnjak.getHP() < 0) {
                    System.out.println("[" + carobnjak.getIme() + "] je pobedjen! ");
                    carobnjak.ubiIgraca();
                    cd.CountDeadPlayersPlus();
                }
                System.out.println( carobnjak.getIme() + " je ostalo: " + carobnjak.getHP() + " HP-a");
            } else {
                String napad = "Bljuvanje vatre";
                carobnjak.primiStetu(20);
                {
                    String zaUnos = ("[Zmaj] je napao ["+carobnjak.getIme() +"] pomocu[" + napad +"]\n");
                    System.out.println(zaUnos);
                    out.write(zaUnos);
                    out.close();
                }
                if (carobnjak.getHP() < 0) {
                    System.out.println("[" + carobnjak.getIme() + "] je pobedjen! ");
                    carobnjak.ubiIgraca();
                    cd.CountDeadPlayersPlus();
                }
                System.out.println( "["+ carobnjak.getIme()+"] je ostalo: " + carobnjak.getHP() + " HP-a");
            }
        }
    }

    public static void CarobnjakborbaSaPaukom(Carobnjak carobnjak, BufferedWriter out, Pauk pauk) throws IOException {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 100 + 1);
        if (randomNum < 50) {
            String napad = "Magija";
            int CarobnjakDamage = 80;
            System.out.println("Uspesno ste napali!");
            pauk.takeDamage(CarobnjakDamage);
            {
                String zaUnos = ("["+ carobnjak.getIme()+"] je napao [Pauka] pomocu[" + napad +"]\n");
                System.out.println(zaUnos);
                out.write(zaUnos);
                out.close();
                if (pauk.getHpPauk() > 0) {
                    System.out.println("[Pauku] je ostalo: " + pauk.getHpPauk() + " HP-a");
                }
            }
            if (pauk.getHpPauk() <= 0) {
                System.out.println("Uspesno ste pobedili pauka! ");
                cd.CountDeadenemiesPlus();;
                pauk.setDeadEnemy();
            }

        } else {
            System.out.println("Primili ste udarac");
            int randomNapad = ThreadLocalRandom.current().nextInt(0, 1 + 1);
            if (randomNapad == 0) {
                String napad = "Obican Napad";
                carobnjak.primiStetu(5);
                {
                    String zaUnos = ("[Pauk] je napao ["+ carobnjak.getIme() +"] pomocu[" + napad +"]\n");
                    System.out.println(zaUnos);
                    out.write(zaUnos);
                    out.close();
                }
                if (carobnjak.getHP() < 0) {
                    System.out.println("[" + carobnjak.getIme() + "] je pobedjen! ");
                    carobnjak.ubiIgraca();
                    cd.CountDeadPlayersPlus();
                }
                System.out.println( carobnjak.getIme() + " je ostalo: " + carobnjak.getHP() + " HP-a");
            } else {
                String napad = "Specijalna mreza";
                carobnjak.primiStetu(20);
                {
                    String zaUnos = ("[Pauk] je napao ["+carobnjak.getIme() +"] pomocu[" + napad +"]\n");
                    System.out.println(zaUnos);
                    out.write(zaUnos);
                    out.close();
                }
                if (carobnjak.getHP() < 0) {
                    System.out.println("[" + carobnjak.getIme() + "] je pobedjen! ");
                    carobnjak.ubiIgraca();
                    cd.CountDeadPlayersPlus();
                }
                System.out.println( "["+ carobnjak.getIme()+"] je ostalo: " + carobnjak.getHP() + " HP-a");
            }
        }
    }

    public static void izaberiProtivnika(Zmaj zmaj, Pauk pauk){
        System.out.println("Izaberite cudoviste koje napadate");
        if(!zmaj.isDead()){
            System.out.println("1.Zmaj");
        }
        if(!pauk.isDead()){
            System.out.println("2.Pauk");
        }
    }

    public static void main(String[] args) throws IOException, NemaMestaException {
        Scanner scanner = new Scanner(System.in);
        Carobnjak carobnjak = new Carobnjak("Carobnjak");
        Macevalac macevalacJedan = new Macevalac("Macevalac Jedan");
        Macevalac macevalacDva = new Macevalac("Macevalac Dva");
        String[] oruzija = {"Mac","Koplje"};

        int countDead = 0;
        int countDeadEnemies = 0;
        Zmaj zmaj = Zmaj.getZmaj();
        Pauk pauk = Pauk.getPauk();
        System.out.println("Dobrodosli u igru");
        while (true){

            BufferedWriter out = new BufferedWriter(new FileWriter("logovanjeAkcija.txt", true));
            if(cd.getCountDeadPlayers() == 3 || cd.getCountDeadEnemies() == 2){
                System.out.println("Igra je gotova!");
                if(cd.getCountDeadPlayers() == 3){
                    System.out.println("Svi Heroji su ubijeni!");
                    System.out.println("Izgubili ste");
                    System.exit(0);
                }else{
                    System.out.println("Cestitamo, oba neprijatelja su pobedjena, presli ste igru!");
                    System.exit(0);
                }
            }else {
                System.out.println("1.Heroj jedan pokupi oruzije");
                System.out.println("2.Heroj jedan baci oruzije");
                System.out.println("3.Heroj dva pokupi oruzije");
                System.out.println("4.Heroj dva baci oruzije");
                if (!macevalacJedan.isDead()) {
                    System.out.println("5.Napad sa herojem jedan");
                }
                if (!macevalacDva.isDead()) {
                    System.out.println("6.Napad sa herojem dva");
                }
                if (!carobnjak.isDead()) {
                    System.out.println("7.Napad sa carobnjakom");
                }
                System.out.println("Unesite komandu(1,2,3,4,5,6,7)");
                int opcija = scanner.nextInt();
                switch (opcija) {
                    case 1:
                        System.out.println("Unesite mesto u inventoy-u(0,1)");
                        int mestoH1Inv = scanner.nextInt();
                        if (macevalacJedan.getInventory(mestoH1Inv) != null) {
                            throw new NemaMestaException("Vec imate oruzije u datom slotu");
//                        System.out.println("Vec imate oruzije u datom slotu");
//                        break;
                        }
                        int wpnCounter1 = 0;
                        for (int i = 0; i < 2; i++) {
                            if (oruzija[i] != null) {
                                System.out.println(i + ".Oruzije: " + oruzija[i]);
                                wpnCounter1++;
                            }
                        }
                        if (wpnCounter1 == 0) {
                            System.out.println("Nema oruzija koje moze da se pokupi");
                            break;
                        }
                        System.out.println("Unesite broj ispred oruzija da bi ga pokupili");
                        int brZaPokupiti = scanner.nextInt();
                        macevalacJedan.dodeliOruzije(oruzija[brZaPokupiti], mestoH1Inv);
                    {
                        String zaUnos = ("["+ macevalacJedan.getIme() + "] je pokupio oružje [" + oruzija[brZaPokupiti] + "]\n");
                        System.out.println(zaUnos);
                        out.write(zaUnos);
                        out.close();
                    }
                    oruzija[brZaPokupiti] = null;
                    break;
                    case 2:
                        int bacanjeCounter1 = 0;
                        for (int i = 0; i < 2; i++) {
                            if (macevalacJedan.getInventory(i) != null) {
                                System.out.println(i + ": " + macevalacJedan.getInventory(i));
                                bacanjeCounter1++;
                            }
                        }
                        if (bacanjeCounter1 == 0) {
                            System.out.println("Nemate ni jedno oruzije");
                            break;
                        }
                        System.out.println("Izaberite oruzije da bacite");
                        int oruzZaBacH1 = scanner.nextInt();
                    {
                        String zaUnos = ("["+ macevalacJedan.getIme() + "] je bacio oruzije [" + macevalacJedan.getInventory(oruzZaBacH1) + "]\n");
                        System.out.println(zaUnos);
                        out.write(zaUnos);
                        out.close();
                    }
                    String sacuvajOr = macevalacJedan.getInventory(oruzZaBacH1);
                    macevalacJedan.baciOruzije(oruzZaBacH1);
                    if (oruzija[0] == null) {
                        oruzija[0] = sacuvajOr;
                    } else if (oruzija[1] == null) {
                        oruzija[1] = sacuvajOr;
                    }
                    break;
                    case 3:
                        System.out.println("Unesite mesto u inventoy-u(0,1)");
                        int mestoH2Inv = scanner.nextInt();
                        if (macevalacDva.getInventory(mestoH2Inv) != null) {
                            System.out.println("Vec imate oruzije u datom slotu");
                            break;
                        }
                        int wpnCounter2 = 0;
                        for (int i = 0; i < 2; i++) {
                            if (oruzija[i] != null) {
                                System.out.println(i + ".Oruzije: " + oruzija[i]);
                                wpnCounter2++;
                            }
                        }
                        if (wpnCounter2 == 0) {
                            System.out.println("Nema oruzija koje moze da se pokupi");
                            break;
                        }
                        System.out.println("Unesite broj ispred oruzija da bi ga pokupili");
                        int brZaPokupitiH2 = scanner.nextInt();
                        macevalacDva.dodeliOruzije(oruzija[brZaPokupitiH2], mestoH2Inv);
                    {
                        String zaUnos = ("["+ macevalacDva.getIme() +"] je pokupio oružje [" + oruzija[brZaPokupitiH2] + "]\n");
                        System.out.println(zaUnos);
                        out.write(zaUnos);
                        out.close();
                    }
                    oruzija[brZaPokupitiH2] = null;
                    break;
                    case 4:
                        int bacanjeCounter2 = 0;
                        for (int i = 0; i < 2; i++) {
                            if (macevalacDva.getInventory(i) != null) {
                                System.out.println(i + ": " + macevalacDva.getInventory(i));
                                bacanjeCounter2++;
                            }
                        }
                        if (bacanjeCounter2 == 0) {
                            System.out.println("Nemate oruzije za bacanje");
                            break;
                        }
                        System.out.println("Izaberite oruzije da bacite");
                        int oruzZaBacH2 = scanner.nextInt();
                    {
                        String zaUnos = ("["+ macevalacDva.getIme() +"] je bacio oruzije [" + macevalacDva.getInventory(oruzZaBacH2) + "]\n");
                        System.out.println(zaUnos);
                        out.write(zaUnos);
                        out.close();
                    }
                    sacuvajOr = macevalacDva.getInventory(oruzZaBacH2);
                    macevalacDva.baciOruzije(oruzZaBacH2);
                    if (oruzija[0] == null) {
                        oruzija[0] = sacuvajOr;
                    } else if (oruzija[1] == null) {
                        oruzija[1] = sacuvajOr;
                    }
                    break;
                    case 5: {
                        int nullCounter = 0;
                        for (int i = 0; i < 2; i++) {
                            if (macevalacJedan.getInventory(i) == null) {
                                nullCounter++;
                            }
                        }
                        if (nullCounter == 2) {
                            System.out.println("Nemate ni jedno oruzije");
                            break;
                        }}
                    System.out.println("Izbaerite oruzije kojim cete napasti");
                    for (int i = 0; i < 2; i++) {
                        if (macevalacJedan.getInventory(i) != null) {
                            System.out.println(i + ": " + macevalacJedan.getInventory(i));
                        }
                    }
                    int oruzijeZaNapadH1 = scanner.nextInt();
                    macevalacJedan.equipWeapon(macevalacJedan.getInventory(oruzijeZaNapadH1));
                    izaberiProtivnika(zmaj,pauk);
                    {
                        int cudZaNapad = scanner.nextInt();
                        switch (cudZaNapad) {
                            case 1:
                                borbaSaZmajem(macevalacJedan, out, zmaj);
                                break;
                            case 2:
                                borbaSaPaukom(macevalacJedan, out, pauk);
                                break;
                            default:
                                System.out.println("Nepostojeca opcija");
                                break;
                        }
                    }
                    break;
                    case 6: {
                        int nullCounter = 0;
                        for (int i = 0; i < 2; i++) {
                            if (macevalacDva.getInventory(i) == null) {
                                nullCounter++;
                            }
                        }
                        if (nullCounter == 2) {
                            System.out.println("Nemate ni jedno oruzije");
                            break;
                        }
                    }
                    System.out.println("Izbaerite oruzije kojim cete napasti");
                    for (int i = 0; i < 2; i++) {
                        if (macevalacDva.getInventory(i) != null) {
                            System.out.println(i + ": " + macevalacDva.getInventory(i));
                        }
                    }
                    int oruzijeZaNapadH2 = scanner.nextInt();
                    macevalacDva.equipWeapon(macevalacDva.getInventory(oruzijeZaNapadH2));
                    izaberiProtivnika(zmaj,pauk);
                    {
                        int cudZaNapad = scanner.nextInt();
                        switch (cudZaNapad) {
                            case 1:
                                borbaSaZmajem(macevalacDva, out, zmaj);
                                break;
                            case 2:
                                borbaSaPaukom(macevalacDva, out, pauk);
                                break;
                            default:
                                System.out.println("Nepostojeca opcija");
                                break;
                        }
                    }
                    break;
                    case 7:
                        izaberiProtivnika(zmaj,pauk);
                    {
                        int cudZaNapad = scanner.nextInt();
                        switch (cudZaNapad) {
                            case 1:
                                CarobnjakborbaSaZmajem(carobnjak,out,zmaj);
                                break;
                            case 2:
                                CarobnjakborbaSaPaukom(carobnjak,out,pauk);
                                break;
                            default:
                                System.out.println("Nepostojeca opcija");
                                break;
                        }
                    }
                    break;
                    default:
                        System.out.println("Nepostojeca opcija");
                        break;
                }
            }
        }
    }
}

public class Mesto {
    public char nazev;
    public boolean isPropojen = false;

    public Mesto(char n){
        nazev = n;
    }

    public void propojit(){
        isPropojen = true;
    }
}

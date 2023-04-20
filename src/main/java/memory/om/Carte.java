package memory.om;

public class Carte {
	private int     valeur;
	private boolean trouvee;
	
	public Carte(int valeur, boolean trouvee) {
		this.valeur = valeur;
		this.trouvee = trouvee;
	}
	public Carte(int valeur) {
		this(valeur, false);
	}
	public boolean equals(Carte carte) {
		return this.valeur == carte.valeur;
	}
	public boolean isTrouvee() {
		return trouvee;
	}
	public void setTrouvee(boolean trouvee) {
		this.trouvee = trouvee;
	}
	public int getValeur() {
		return this.valeur;
	}
	
}

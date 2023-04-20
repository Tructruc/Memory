package memory.om;

public enum Reponse {
	ERREUR,		// Erreur : il n'est pas possible de jouer la carte proposée
	PREMIERE, 	// La carte proposée est la première
	GAGNE,      // La carte proposée est identique à la première (c'est un coup gagnant)
	PERDU		// La carte proposée est différente de la première (c'est un coup perdant)
}

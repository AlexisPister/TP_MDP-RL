package pacman.environnementRL;

import java.util.ArrayList;
import java.util.Arrays;

import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import environnement.Etat;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */
public class EtatPacmanMDPClassic implements Etat , Cloneable{
	
	private int Nghosts;
	private ArrayList pacmanPosition;
	private ArrayList ghostPositions;
	private int closestDot;
	
	public EtatPacmanMDPClassic(StateGamePacman _stategamepacman){
		
		
		
		// Position du pacman
		pacmanPosition = new ArrayList();
		pacmanPosition.add(_stategamepacman.getPacmanState(0).getX());
		pacmanPosition.add(_stategamepacman.getPacmanState(0).getY());
		
		// Nb de fantomes
		Nghosts = _stategamepacman.getNumberOfGhosts();
		
		// Position des fantomes
		ghostPositions = new ArrayList();
		for(int i = 0; i < Nghosts; ++i) {
			// Coordinates of ghost i
			ArrayList posGhost = new ArrayList();
			posGhost.add(_stategamepacman.getGhostState(i).getX());
			posGhost.add(_stategamepacman.getGhostState(i).getY());
			
			// Save coordinates of all ghosts
			ghostPositions.add(posGhost);
		}
		
		// Point le plus proche
		closestDot = _stategamepacman.getClosestDot(
				_stategamepacman.getPacmanState(0));
		
		
		
		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Nghosts;
		result = prime * result + ((pacmanPosition == null) ? 0 : pacmanPosition.hashCode());
		result = prime * result + closestDot;
		result = prime * result + ((ghostPositions == null) ? 0 : ghostPositions.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		
		return "yo";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EtatPacmanMDPClassic other = (EtatPacmanMDPClassic) obj;
		if (Nghosts != other.Nghosts)
			return false;
		if (closestDot != other.closestDot)
			return false;
		if (pacmanPosition == null) {
			if (other.pacmanPosition != null)
				return false;
		} else if (!pacmanPosition.equals(other.pacmanPosition))
				return false;
		if (ghostPositions == null) {
			if (other.ghostPositions != null)
				return false;
		} else if (!ghostPositions.equals(other.ghostPositions))
				return false;
		return true;
	}
	
	
	public Object clone() {
		EtatPacmanMDPClassic clone = null;
		try {
			// On recupere l'instance a renvoyer par l'appel de la 
			// methode super.clone()
			clone = (EtatPacmanMDPClassic)super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implementons 
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}
		


		// on renvoie le clone
		return clone;
	}



	

}

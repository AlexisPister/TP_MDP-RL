package agent.rlagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.util.Pair;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
/**
 * Renvoi 0 pour valeurs initiales de Q
 * @author laetitiamatignon
 *
 */
public class QLearningAgent extends RLAgent {
	/**
	 *  format de memorisation des Q valeurs: utiliser partout setQValeur car cette methode notifie la vue
	 */
	protected HashMap<Etat,HashMap<Action,Double>> qvaleurs;
	
	//AU CHOIX: vous pouvez utiliser une Map avec des Pair pour clés si vous préférez
	//protected HashMap<Pair<Etat,Action>,Double> qvaleurs;

	
	/**
	 * 
	 * @param alpha
	 * @param gamma
	 * @param Environnement
	 * @param nbS attention ici il faut tous les etats (meme obstacles) car Q avec tableau ...
	 * @param nbA
	 */
	public QLearningAgent(double alpha, double gamma,
			Environnement _env) {
		super(alpha, gamma,_env);
		qvaleurs = new HashMap<Etat,HashMap<Action,Double>>();
		
		
	
	}


	
	
	/**
	 * renvoi action(s) de plus forte(s) valeur(s) dans l'etat e
	 *  (plusieurs actions sont renvoyees si valeurs identiques)
	 *  renvoi liste vide si aucunes actions possibles dans l'etat (par ex. etat absorbant)

	 */
	@Override
	public List<Action> getPolitique(Etat e) {
		// retourne action de meilleures valeurs dans _e selon Q : utiliser getQValeur()
		// retourne liste vide si aucune action legale (etat terminal)
		List<Action> returnactions = new ArrayList<Action>();
		Action best_action = null;
		
		if (this.getActionsLegales(e).size() == 0){//etat  absorbant; impossible de le verifier via environnement
			System.out.println("aucune action legale");
			return new ArrayList<Action>();
		}
		
		//*** VOTRE CODE
		
		// Trouve la valeur maximale
		HashMap<Action,Double> actions = qvaleurs.get(e);
		double max = Double.NEGATIVE_INFINITY;
		for(Action a: this.getActionsLegales(e)) {
			if (getQValeur(e, a) > max) {
				max = getQValeur(e, a);
			}
		}
		
		// Renvoie une liste des meilleurs actions
		for(Action a: this.getActionsLegales(e)) {
			if (getQValeur(e, a) == max) {
				returnactions.add(a);
			}
		}
		
		
		return returnactions;
		
		
	}
	
	@Override
	public double getValeur(Etat e) {
		//*** VOTRE CODE
		if(qvaleurs.containsKey(e)) {
			double max = Double.NEGATIVE_INFINITY;
			for (Action key: qvaleurs.get(e).keySet()) {
				if (max < qvaleurs.get(e).get(key)) {
					max = qvaleurs.get(e).get(key);
				}
			}
			return max;
		}
		else {
			return 0.0;
		}
	}

	@Override
	public double getQValeur(Etat e, Action a) {
		//*** VOTRE CODE
		if (qvaleurs.containsKey(e)) {
			if (qvaleurs.get(e).containsKey(a)) {
				//System.out.println(qvaleurs.get(e));
				//System.out.println(qvaleurs.get(e).get(a));
				return qvaleurs.get(e).get(a);
			} else {
				HashMap<Action, Double> inner_map = qvaleurs.get(e);
				inner_map.put(a, 0.0);
				qvaleurs.put(e,  inner_map);
				
			}
		} else {
			HashMap<Action, Double> Hash = new HashMap<Action, Double>();
			Hash.put(a, 0.0);
			qvaleurs.put(e, Hash);
		}
		return(0.0);
	}
	
	
	
	@Override
	public void setQValeur(Etat e, Action a, double d) {
		//*** VOTRE CODE
		HashMap<Action,Double> temp_map = qvaleurs.get(e);
		temp_map.put(a, d);
		qvaleurs.put(e, temp_map);
		
		// mise a jour vmax et vmin pour affichage du gradient de couleur:
				//vmax est la valeur de max pour tout s de V
				//vmin est la valeur de min pour tout s de V
				// ...
		for(Etat et:qvaleurs.keySet()) {
			double value = getValeur(e);
			if (value < vmin) {
				vmin = value;
			} else if (value > vmax) {
				vmax = value;
			}
		}
		
		
		this.notifyObs();
		
	}
	
	
	/**
	 * mise a jour du couple etat-valeur (e,a) apres chaque interaction <etat e,action a, etatsuivant esuivant, recompense reward>
	 * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement apres avoir realise une action.
	 * @param e
	 * @param a
	 * @param esuivant
	 * @param reward
	 */
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		if (RLAgent.DISPRL)
			System.out.println("QL mise a jour etat "+e+" action "+a+" etat' "+esuivant+ " r "+reward);

		//*** VOTRE CODE
		double Qvalue = getQValeur(e, a);
		double new_value = (1 - alpha) * Qvalue + alpha * (reward + gamma * getValeur(esuivant));
		setQValeur(e, a, new_value);
		
	}

	@Override
	public Action getAction(Etat e) {
		this.actionChoisie = this.stratExplorationCourante.getAction(e);
		return this.actionChoisie;
	}

	@Override
	public void reset() {
		super.reset();
		//*** VOTRE CODE
		qvaleurs = new HashMap<Etat,HashMap<Action,Double>>();
		
		
		this.episodeNb = 0;
		this.notifyObs();
	}









	


}

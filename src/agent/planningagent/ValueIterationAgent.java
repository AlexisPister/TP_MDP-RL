package agent.planningagent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import util.HashMapUtil;

import java.util.HashMap;

import environnement.Action;
import environnement.Etat;
import environnement.IllegalActionException;
import environnement.MDP;
import environnement.Action2D;


/**
 * Cet agent met a jour sa fonction de valeur avec value iteration 
 * et choisit ses actions selon la politique calculee.
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent{
	/**
	 * discount facteur
	 */
	protected double gamma;

	/**
	 * fonction de valeur des etats
	 */
	protected HashMap<Etat,Double> V;

	
	/**
	 * 
	 * @param gamma
	 * @param nbIterations
	 * @param mdp
	 */
	
	
	public ValueIterationAgent(double gamma,  MDP mdp) {
		super(mdp);
		this.gamma = gamma;
		//*** VOTRE CODE
		V = new HashMap<Etat,Double>();
		// On rempli la grille avec des 0
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
		}
	}
	
	
	
	
	public ValueIterationAgent(MDP mdp) {
		this(0.9,mdp);
	}
	
	/**
	 * 
	 * 
	 * Retourne le maximum d'une ArrayList de double
	 */
	public Double maximum(ArrayList<Double> list) {
		Double max = Double.NEGATIVE_INFINITY;
		for(double el:list) {
			if(el > max) {
				max = el;
			}
		}
		return (max);
	}
	
	/**
	 * 
	 * Retourne une map contenant le terme de la somme 
	 * de l'algo Value Iteration pour chaque action
	 */
	public HashMap<Action, Double> sum_VI(Etat etat){
		// Liste des actions possibles
		List<Action> Actions = this.mdp.getActionsPossibles(etat);
		
		// Hashmap entre les actions et l'estimation de la formule
		HashMap<Action, Double> Results = new HashMap<Action, Double>();
		
		for(Action action:Actions) {
			// Pour chaque action, calculer la somme
			Double sum = 0.0; // 
			try {
				// Ensemble des s'
				Map<Etat, Double> etats_transition = mdp.getEtatTransitionProba(etat, action);
				for(Etat eprime:etats_transition.keySet()) {
					// Pour chaque s' (etats voisins de s)
					Double T = etats_transition.get(eprime); // T
					Double R = mdp.getRecompense(etat, action, eprime); // R
					
					// Incremente la somme pour s'
					sum += T * (R + gamma * V.get(eprime));
				}
		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Results.put(action, sum);
		}
		
		return(Results);
	}
	
	/**
	 * 
	 * Mise a jour de V: effectue UNE iteration de value iteration (calcule V_k(s) en fonction de V_{k-1}(s'))
	 * et notifie ses observateurs.
	 * Ce n'est pas la version inplace (qui utilise nouvelle valeur de V pour mettre a jour ...)
	 */
	@Override
	public void updateV(){
		//delta est utilise pour detecter la convergence de l'algorithme
		//lorsque l'on planifie jusqu'a convergence, on arrete les iterations lorsque
		//delta < epsilon 
		this.delta=0.0;
		//*** VOTRE CODE
		
		// On copie la grille pour que toutes les cases soient mises a jour en meme temps
		
		HashMap<Etat,Double> V2 = new HashMap<Etat, Double>();
		
		for(Etat etat:V.keySet()) {
			if (mdp.estAbsorbant(etat)) {
				V2.put(etat, 0.0);
			}
			else {
				HashMap<Action, Double> results_actions = sum_VI(etat);
				
				// Get the maximum value among actions
				Double max = Double.NEGATIVE_INFINITY;
				for(Double s:results_actions.values()) {
					if(s > max) {
						max = s;
					}
				}
				
				// update s in V
				V2.put(etat, max);
				
			}
		}
		
		
		/*
		for(Etat etat:V.keySet()) {
			if (mdp.estAbsorbant(etat)) {
				V2.put(etat, 0.0);
			}
			else {
				List<Action> Actions = this.mdp.getActionsPossibles(etat);
				//terme de la somme dans la formule pour chaque a
				ArrayList<Double> estimations = new ArrayList<Double>();
				
				for(Action action:Actions) {
					// Pour chaque action, calculer la somme
					Double sum = 0.0; // 
					try {
						// Ensemble des s'
						Map<Etat, Double> etats_transition = mdp.getEtatTransitionProba(etat, action);
						for(Etat eprime:etats_transition.keySet()) {
							// Pour chaque s' (etats voisins de s)
							Double T = etats_transition.get(eprime); // T
							Double R = mdp.getRecompense(etat, action, eprime); // R
							
							// Incremente la somme pour s'
							sum += T * (R + gamma * V.get(eprime));
						}
				
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					estimations.add(sum);
				}
				
				// get the maximum
				Double max = maximum(estimations);
				
				// Update s
				V2.put(etat, max);
			}
			
		}
		*/
		
		// Mise a jour de delta
		ArrayList<Double> diffs = new ArrayList<Double>();
		for(Etat s:V.keySet()) {
			Double diff = V.get(s) - V2.get(s);
			System.out.print(diff);
			diffs.add(Math.abs(diff));
		}
		delta = maximum(diffs);
		System.out.println(delta);
				
		// Replace the grill
		V = V2;
		
		
		// mise a jour vmax et vmin pour affichage du gradient de couleur:
		//vmax est la valeur de max pour tout s de V
		//vmin est la valeur de min pour tout s de V
		// ...
		for(Double value:V.values()) {
			if (value < vmin) {
				vmin = value;
			} else if (value > vmax) {
				vmax = value;
			}
		}
		//******************* laisser notification a la fin de la methode	
		this.notifyObs();
	}
	
	
	/**
	 * renvoi l'action executee par l'agent dans l'etat e 
	 * Si aucune actions possibles, renvoi Action2D.NONE
	 */
	@Override
	public Action getAction(Etat e) {
		//*** VOTRE CODE
		//Meilleur(s) action(s) pour l'etat e
		List<Action> best_actions = getPolitique(e);
		
		// Select random action if there are several equivalent actions
		if(best_actions.size() > 0) {
			Action action = best_actions.get(new Random().nextInt(best_actions.size()));
			return action;
		} else {
			return Action2D.NONE;
		}
		
	}
	// Retourne les valeurs sur la grille
	@Override
	public double getValeur(Etat _e) {
		//*** VOTRE CODE
		
		if(V.containsKey(_e)) {
			return V.get(_e);
		} else {
			return 0.0;
		}
	}
	/**
	 * renvoi action(s) de plus forte(s) valeur(s) dans etat 
	 * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
	 */
	@Override
	public List<Action> getPolitique(Etat _e) {
		//*** VOTRE CODE
		
		// retourne action de meilleure valeur dans _e selon V, 
		// retourne liste vide si aucune action legale (etat absorbant)
		List<Action> returnactions = new ArrayList<Action>();
		
		// Hashmap action > somme de la formule
		HashMap<Action, Double> sum_actions = sum_VI(_e);
		
		// Enregiste le maximum de la liste
		Double max_value = Double.NEGATIVE_INFINITY;
		for(Action a:sum_actions.keySet()) {
			if (sum_actions.get(a) > max_value) {
				max_value = sum_actions.get(a);
			}
		}
		
		// Selectionne toutes les actions ayant la valeur max
		for(Action a:sum_actions.keySet()) {
			if (sum_actions.get(a) == max_value) {
				returnactions.add(a);
			}
		}
		return returnactions;
	}
	
	@Override
	public void reset() {
		super.reset();

		
		this.V.clear();
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
		}
		// ADD
		// System.out.println(mdp.getEtatsAccessibles());
		
		this.notifyObs();
	}

	

	

	public HashMap<Etat,Double> getV() {
		return V;
	}
	public double getGamma() {
		return gamma;
	}
	@Override
	public void setGamma(double _g){
		System.out.println("gamma= "+gamma);
		this.gamma = _g;
	}


	
	

	
}

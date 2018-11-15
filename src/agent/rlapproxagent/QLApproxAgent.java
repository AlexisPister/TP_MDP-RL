package agent.rlapproxagent;


import java.util.ArrayList;
import java.util.List;

import agent.rlagent.QLearningAgent;
import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
/**
 * Agent qui apprend avec QLearning en utilisant approximation de la Q-valeur : 
 * approximation lineaire de fonctions caracteristiques 
 * 
 * @author laetitiamatignon
 *
 */
public class QLApproxAgent extends QLearningAgent{
	
	private ArrayList<Double> thetas;
	private FeatureFunction featurefunction;
	
	public QLApproxAgent(double alpha, double gamma, Environnement _env,FeatureFunction _featurefunction) {
		super(alpha, gamma, _env);
		//*** VOTRE CODE
		
		// Initialisation des poids
		thetas = new ArrayList<Double>();
		for(int i = 0; i < featurefunction.getFeatureNb(); ++i) {
			thetas.add(0.5);
		}
		
		
	}

	
	@Override
	public double getQValeur(Etat e, Action a) {
		//*** VOTRE CODE
		double Q = 0.0;
		double[] features = featurefunction.getFeatures(e, a);
		for(int i = 0; i < featurefunction.getFeatureNb(); ++i) {
			Q += thetas.get(i) * features[i];
		}
		
		return Q;

	}
	
	
	
	
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		if (RLAgent.DISPRL){
			System.out.println("QL: mise a jour poids pour etat \n"+e+" action "+a+" etat' \n"+esuivant+ " r "+reward);
		}
       //inutile de verifier si e etat absorbant car dans runEpisode et threadepisode 
		//arrete episode lq etat courant absorbant	
		
		//*** VOTRE CODE
		
		
	}
	
	@Override
	public void reset() {
		super.reset();
		this.qvaleurs.clear();
	
		//*** VOTRE CODE
		
		this.episodeNb =0;
		this.notifyObs();
	}
	
	
}

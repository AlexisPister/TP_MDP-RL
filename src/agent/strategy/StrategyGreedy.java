package agent.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Etat;
/**
 * Strategie qui renvoit un choix aleatoire avec proba epsilon, un choix glouton (suit la politique de l'agent) sinon
 * @author lmatignon
 *
 */
public class StrategyGreedy extends StrategyExploration{
	/**
	 * parametre pour probabilite d'exploration
	 */
	protected double epsilon;
	private Random rand=new Random();
	
	
	
	public StrategyGreedy(RLAgent agent,double epsilon) {
		super(agent);
		this.epsilon = epsilon;
	}

	@Override
	public Action getAction(Etat _e) {//renvoi null si _e absorbant
		double d =rand.nextDouble();
		List<Action> actions;
		if (this.agent.getActionsLegales(_e).isEmpty()){
			return null;
		}
	
		//VOTRE CODE ICI
		double rand = Math.random();
		if(rand < epsilon) {
			// Proba epsilon de faire un deplacement aleatoire
			Random rando = new Random();
			List<Action> keys = new ArrayList<Action>(this.agent.getActionsLegales(_e));
			Action rkey = keys.get(rando.nextInt(keys.size()));
			return rkey;
		}
		// A FAIRE
		else {
			// Proba 1 - epsilon de faire un choisir la meilleure action (choisi au hasard si plusieurs possibles)
			int random = ThreadLocalRandom.current().nextInt(0, this.agent.getPolitique(_e).size());
			return this.agent.getPolitique(_e).get(random);
		}
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
		System.out.println("epsilon:"+epsilon);
	}

/*	@Override
	public void setAction(Action _a) {
		// TODO Auto-generated method stub
		
	}*/

}

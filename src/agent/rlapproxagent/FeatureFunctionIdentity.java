package agent.rlapproxagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import environnement.Action;
import environnement.Action2D;
import environnement.Etat;
import javafx.util.Pair;
/**
 * Vecteur de fonctions caracteristiques phi_i(s,a): autant de fonctions caracteristiques que de paire (s,a),
 * <li> pour chaque paire (s,a), un seul phi_i qui vaut 1  (vecteur avec un seul 1 et des 0 sinon).
 * <li> pas de biais ici 
 * 
 * @author laetitiamatignon
 *
 */
public class FeatureFunctionIdentity implements FeatureFunction {
	//*** VOTRE CODE
	// Nb de features
	int Nfeatures;
	// Associe à chaque couple (e,a) l'indice du feature correspondant dans le tableau des features
	HashMap<Etat, HashMap<Action, Integer>> ind_features;
	// Vecteur des features
	double[] features;
	// Indice de features
	int indice;
	
	public FeatureFunctionIdentity(int _nbEtat, int _nbAction){
		//*** VOTRE CODE
		Nfeatures = _nbEtat * _nbAction;
		features = new double[Nfeatures];
		indice = 0;
	}
	
	@Override
	public int getFeatureNb() {
		//*** VOTRE CODE
		return Nfeatures;
	}

	@Override
	public double[] getFeatures(Etat e,Action a){
		//*** VOTRE CODE
		if(!ind_features.containsKey(e)) {
			ind_features.put(e, new HashMap<Action, Integer>());
			
		}
		HashMap<Action, Integer> ajout = ind_features.get(e);
		if(!ajout.containsKey(a)) {
			ajout.put(a, indice);
			ind_features.put(e, ajout);
			indice += 1;
		}
		
		// Remet la liste des features a 0
		for(int i = 0; i < features.length; ++i) {
			features[i] = 0;
		}
		
		// Met le 1 au feature associé au couple (e,a) demandé
		features[ind_features.get(e).get(a)] = 1;
		
		
//		else {
//			Set cles = ajout.keySet();
//			Iterator it = cles.iterator();
//			while(it.hasNext()) {
//				Action act = (Action) it.next();
//				if(act == a) {
//					ajout.remove(act);
//					ajout.put(a, 1);
//				}
//				else {
//					ajout.remove(act);
//					ajout.put(act, 0);
//					
//				}
//			}
//		}
		
		return features;
	}
	

}

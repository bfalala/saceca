/*
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike
 * 3.0 Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter to Creative Commons,
 * 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * The original Urban 3 Dimensional Universe application was created by Sylvain Cambon,
 * Aurélien Chabot, Anthony Foulfoin, Jérôme Dalbert & Johann Legaye.
 * Contact them for other licensing possibilities, using this email address pattern:
 * <first_name> DOT <name> AT etu DOT enseeiht DOT fr .
 * http://www.projet.long.2011.free.fr
 */
package fr.n7.saceca.u3du.model.ai.agent.module.reasoning.manageGoal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.Emotion;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.MMGoal;
import fr.n7.saceca.u3du.model.ai.service.Param;

/**
 * The CreateEmotionGoal class - creates a reactive goal using the emotions values
 * 
 * @author Ciprian Munteanu, Mehdi Boukhris
 * 
 */
public class CreateEmotionGoal extends SameElementsRule<Emotion> {
	/**
	 * Constructor
	 * 
	 * @param agent
	 *            the agent
	 */
	public CreateEmotionGoal(Agent agent) {
		super(agent);
	}
	
	@Override
	public void reset() {
		this.elementsToCheck = new ArrayList<Emotion>();
		for (Emotion emotion : this.agent.getEmotions()) {
			if (emotion.getValue() >= Emotion.ALERT_LEVEL) {
				this.elementsToCheck.add(emotion);
			}
		}
	}
	
	@Override
	protected boolean checkConditions() {
		if (!this.hasElementsToCheck()) {
			return false;
		}
		// sorts the list of emotions in reverse order of their values
		Collections.sort(this.elementsToCheck, Collections.reverseOrder(new Comparator<Emotion>() {
			@Override
			public int compare(Emotion e1, Emotion e2) {
				return e1.getValue().compareTo(e2.getValue());
			}
		}));
		
		return true;
	}
	
	@Override
	protected void applyEffects() {
		// takes the emotion with the highest value
		Emotion maxEmotion = this.elementsToCheck.get(0);
		// creates a new reactive goal from this emotion
		MMGoal goalToAdd = new MMGoal();
		
		goalToAdd.getSuccessCondition().setPropertyName("reactive_emotion_" + maxEmotion.getNameSuffix());
		goalToAdd.getSuccessCondition().setPropertyParameters(new ArrayList<Param>());
		
		goalToAdd.setPriority((int) Math.ceil(maxEmotion.getValue()));
		
		// add the created goal in the stack of goals
		this.agent.getMemory().getGoalStack().addMiddle(goalToAdd, "emotion");
	}
}

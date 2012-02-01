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
package fr.n7.saceca.u3du.model.io.ai.statement;

import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import fr.n7.saceca.u3du.model.ai.statement.Condition;
import fr.n7.saceca.u3du.model.ai.statement.Effect;
import fr.n7.saceca.u3du.model.ai.statement.StatementFactory;
import fr.n7.saceca.u3du.model.ai.statement.StatementsGroup;

/**
 * The Class StatementsGroupConverter.
 * 
 * @see StatementsGroup
 * @see <a href=
 *      "http://xstream.codehaus.org/javadoc/com/thoughtworks/xstream/converters/Converter.html"
 *      >com.thoughtworks.xstream.converters.Converter</a>
 * 
 * @author Sylvain Cambon
 */
public class StatementsGroupConverter implements Converter {
	
	/** The Constant CONDITIONS. */
	private static final String CONDITIONS = "conditions";
	
	/** The Constant CONDITION. */
	private static final String CONDITION = "condition";
	
	/** The Constant EFFECTS. */
	private static final String EFFECTS = "effects";
	
	/** The Constant EFFECT. */
	private static final String EFFECT = "effect";
	
	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
		return StatementsGroup.class.equals(type);
	}
	
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		StatementsGroup statements = (StatementsGroup) source;
		
		// Conditions
		writer.startNode(CONDITIONS);
		for (Condition test : statements.getTests()) {
			writer.startNode(CONDITION);
			writer.setValue(test.toString());
			writer.endNode();
		}
		writer.endNode();
		
		// Actions
		writer.startNode(EFFECTS);
		for (Effect action : statements.getActions()) {
			writer.startNode(EFFECT);
			writer.setValue(action.toString());
			writer.endNode();
		}
		writer.endNode();
	}
	
	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		StatementFactory factory = StatementFactory.getInstance();
		
		// Conditions
		List<Condition> tests = new LinkedList<Condition>();
		reader.moveDown();
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			tests.add(factory.createTestStatement(reader.getValue()));
			reader.moveUp();
		}
		reader.moveUp();
		
		// Actions
		List<Effect> actions = new LinkedList<Effect>();
		reader.moveDown();
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			actions.add(factory.createActionStatement(reader.getValue()));
			reader.moveUp();
		}
		reader.moveUp();
		
		return new StatementsGroup(tests, actions);
	}
}

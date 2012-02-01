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
package fr.n7.saceca.u3du.model.io.ai.instance.agent;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import fr.n7.saceca.u3du.exception.MalformedObjectException;
import fr.n7.saceca.u3du.model.ai.EntitiesFactory;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.memory.Memory;
import fr.n7.saceca.u3du.model.io.ai.instance.object.WorldObjectConverter;
import fr.n7.saceca.u3du.model.util.IDProvider;

/**
 * An agent instance converter.
 * 
 * @author Sylvain Cambon
 */
public class AgentConverter extends WorldObjectConverter {
	
	/** The Constant MEMORIES_NODE. */
	private static final String MEMORIES_NODE = "memory";
	
	/**
	 * Instantiates a new agent converter.
	 * 
	 * @param factory
	 *            the factory
	 */
	public AgentConverter(EntitiesFactory factory) {
		super(factory);
	}
	
	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
		return Agent.class.equals(type);
	}
	
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		Agent object = (Agent) source;
		
		// Calling the marshal method for World Objects, which handles common attributes
		super.marshal(object, writer, context);
		
		writer.startNode(MEMORIES_NODE);
		context.convertAnother(object.getMemory());
		writer.endNode();
	}
	
	/**
	 * Reads an instance from XML. The belongings cannot be completely initialized, thus a later
	 * binding mechanism is required. World objects which model is null have to be replaced by the
	 * instances having the same ID. This mechanism is implemented in the
	 * <code>EntitiesFactory.repairBelongings</code>.
	 * 
	 * @see EntitiesFactory#repairBelongings(java.util.Collection)
	 * @param reader
	 *            the XStream reader
	 * @param context
	 *            the XStream context
	 * @return the read object or null if a problem arose.
	 */
	@Override
	public Agent unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		// Model
		String modelName = this.readModelAttribute(reader);
		
		// Building the initial object
		Agent agent;
		try {
			agent = this.getFactory().createAgent(modelName);
		} catch (MalformedObjectException e) {
			throw new ConversionException("The agent cannot be built properly", e);
		}
		if (agent == null) {
			throw new ConversionException("The factory produced a null agent.");
		}
		
		// ID
		long id = this.readIdAttribute(reader, modelName, agent);
		IDProvider.INSTANCE.tellAboutUsage(id);
		
		// The short string representation can be built to help debugging the correct xml file.
		String shortString = modelName + " (id=" + id + ")";
		
		// Position
		this.readPositionNode(reader, context, agent, shortString);
		
		// Properties
		this.readPropertiesNode(reader, agent, shortString);
		
		// Belongings
		this.readBelongingsNode(reader, agent, shortString);
		
		// Memory
		this.readMemoryNode(reader, context, agent, shortString);
		
		return agent;
	}
	
	/**
	 * <p>
	 * Reads the memory node.
	 * </p>
	 * <p>
	 * A ConversionException is thrown if a problem arose during the conversion.
	 * </p>
	 * 
	 * @param reader
	 *            the reader
	 * @param context
	 *            the context
	 * @param agent
	 *            the agent
	 * @param shortString
	 *            the short string
	 */
	protected void readMemoryNode(HierarchicalStreamReader reader, UnmarshallingContext context, Agent agent,
			String shortString) {
		reader.moveDown();
		String nodeName = reader.getNodeName();
		if (!MEMORIES_NODE.equals(nodeName)) {
			throw new ConversionException("The expected node was \"" + MEMORIES_NODE + "\" but was instead: \""
					+ nodeName + "\" in " + shortString + ".");
		}
		Memory memory = (Memory) context.convertAnother(agent, Memory.class);
		if (memory == null) {
			throw new ConversionException("The memory of " + shortString + " was null.");
		}
		agent.setMemory(memory);
		memory.setAgent(agent);
		reader.moveUp();
	}
}

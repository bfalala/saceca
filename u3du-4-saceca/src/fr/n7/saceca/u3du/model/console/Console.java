/*
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike
 * 3.0 Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter to Creative Commons,
 * 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * The original Urban 3 Dimensional Universe application was created by Sylvain Cambon,
 * AurÃ©lien Chabot, Anthony Foulfoin, JÃ©rÃ´me Dalbert & Johann Legaye.
 * Contact them for other licensing possibilities, using this email address pattern:
 * <first_name> DOT <name> AT etu DOT enseeiht DOT fr .
 * http://www.projet.long.2011.free.fr
 */
package fr.n7.saceca.u3du.model.console;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.vecmath.Point2f;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.Internal;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;

/**
 * The Class Console for entering commands to the agents.
 * 
 * @author JÃ©rÃ´me Dalbert
 */
public class Console {
	
	/**
	 * Execute several commands.
	 * 
	 * @param commands
	 *            the commands
	 * @return the optional output of the commands
	 * @throws CommandException
	 *             the saceca strict exception
	 */
	public String executeCommands(String commands) throws CommandException {
		commands = commands.replace("\n", "");
		List<Agent> clearedPlanAgents = new ArrayList<Agent>();
		String output = "";
		
		// The separator for commands is ";"
		for (String command : commands.split(" *; *")) {
			output += this.executeCommand(command, clearedPlanAgents);
		}
		
		return output;
	}
	
	/**
	 * Execute a command.
	 * 
	 * @param command
	 *            the command
	 * @param clearedPlanAgents
	 *            the agents whose plan is already cleared by a previous call to executeCommand
	 * @return the optional output
	 * @throws CommandException
	 *             the saceca strict exception
	 */
	private String executeCommand(String command, List<Agent> clearedPlanAgents) throws CommandException {
		if (command.equals("help") || command.equals("usage")) {
			return this.usage();
		}
		
		/* Parsing (lexing + syntax analysing, thanks to regular expressions) */
		Pattern p = Pattern.compile("(agent )?(\\w+) (\\w+) ?(.+)?");
		Matcher m = p.matcher(command);
		
		if (!m.find()) {
			throw new CommandException("The syntax is incorrect.");
		}
		
		/* Semantic check */

		// We get the agent id
		long id = -1;
		String idOrName = m.group(2);
		// Case when idOrName is an id
		try {
			id = Long.parseLong(idOrName);
		}
		// Case when idOrName is a name
		catch (NumberFormatException e) {
			for (Agent agent : Model.getInstance().getAI().getWorld().getAgents()) {
				String agentName;
				try {
					agentName = agent.getPropertiesContainer().getString(Internal.Agent.NAME);
					if (idOrName.toLowerCase().equals(agentName.toLowerCase())) {
						id = agent.getId();
					}
				} catch (UnknownPropertyException e1) {
					e1.printStackTrace();
				}
			}
			if (id == -1) {
				throw new CommandException("There is no agent with name " + idOrName);
			}
		}
		
		// We get the action or service name
		String actionName = m.group(3);
		
		// We get the service parameters
		List<Object> params = new ArrayList<Object>();
		if (m.group(4) != null) {
			// The separator for params is "_"
			for (String param : m.group(4).split(" *_ *")) {
				params.add(this.convertParam(param));
			}
		}
		
		// We get the agent
		Object o = Model.getInstance().getAI().getWorld().getWorldObjects().get(id);
		if (o == null || !(o instanceof Agent)) {
			throw new CommandException("There is no agent with id " + id);
		}
		Agent agent = (Agent) o;
		
		/* Command execution */
		boolean clearPreviousPlan = true;
		if (clearedPlanAgents.contains(agent)) {
			clearPreviousPlan = false;
		} else {
			clearedPlanAgents.add(agent);
		}
		
		if (actionName.equals("resume")) {
			agent.getPlanningModule().enablePlanning();
		} else if (actionName.equals("kill")) {
			agent.kill();
		} else if (actionName.equals("revive")) {
			agent.revive();
		} else {
			agent.getPlanningModule().forceExecution(actionName, params, clearPreviousPlan);
		}
		
		return "";
	}
	
	/**
	 * Converts a param to a more appropriate type, if possible.
	 * 
	 * @param param
	 *            the param
	 * @return the object
	 */
	private Object convertParam(String param) {
		// If the param is a destination, we return the closest sidewalk to it
		Pattern p = Pattern.compile("(-?\\d+),(-?\\d+)");
		Matcher m = p.matcher(param);
		if (m.find()) {
			Point2f destinationPoint = new Point2f(Float.parseFloat(m.group(1)), Float.parseFloat(m.group(2)));
			return Model.getInstance().getAI().getWorld().getClosestWalkable(destinationPoint);
		}
		
		// If the param is a world object id, we return it
		p = Pattern.compile("(object )?(\\d+)");
		m = p.matcher(param);
		if (m.find()) {
			long id = Long.parseLong(m.group(2));
			return Model.getInstance().getAI().getWorld().getWorldObjects().get(id);
		}
		
		// If no pattern is found, we return the param as it is
		return param;
	}
	
	/**
	 * The console usage.
	 * 
	 * @return the usage
	 */
	private String usage() {
		StringBuilder msg = new StringBuilder();
		
		msg.append("The syntax for a command is : [agent] <agent_name_or_id> <action> [<action_params>]\n");
		msg.append("Where :\n");
		msg.append("- action is either a service name (case insensitive) or \"resume\" (causing the agent to resume its autonomous planning) or \"kill\" or \"revive\".\n");
		msg.append("- action_params is a set of parameters, separated with \"_\".");
		msg.append(" A parameter is either a position (syntax \"<intX>,<intY>\") or a world object ID (syntax \"<objectID>\" or \"object <objectID>\").\n");
		msg.append("Several commands can be entered at once, by separating them with \";\".\n");
		msg.append("A set of commands entered for a given agent cancels the previous commands on this agent.");
		
		return msg.toString();
	}
}
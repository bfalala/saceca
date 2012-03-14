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
package fr.n7.saceca.u3du.model.ai.agent.module.planning;

import java.util.ArrayList;

import Emotion_secondary.update_secondary;

import fr.n7.saceca.u3du.model.ai.agent.memory.Memory;
import fr.n7.saceca.u3du.model.ai.agent.module.planning.initialization.Matrix;
import fr.n7.saceca.u3du.model.ai.agent.module.planning.initialization.TableClass;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.MMGoal;
import fr.n7.saceca.u3du.model.ai.service.Param;
import fr.n7.saceca.u3du.model.ai.service.PropertyLink;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.ai.service.ServiceProperty;
import fr.n7.saceca.u3du.model.util.Couple;

/**
 * The MatrixMethodPlanner class
 * 
 * @author Ciprian Munteanu, Mehdi Boukhris
 * 
 */
public class MatrixMethodPlanner {
	/** The virtual memory */
	private Memory virtualMemory;
	
	/** The goal to reach */
	private ServiceProperty goalToReach;
	
	/** The list of service properties */
	private ArrayList<ServiceProperty> servicePropertyList;
	
	/** The list of services */
	private ArrayList<Service> serviceList;
	
	/** The list of links */
	private ArrayList<PropertyLink> linkList;
	
	/** The matrix Services X Effects */
	private Matrix matrixServProp;
	
	/** The matrix Preconditionx X Services */
	private Matrix matrixCondServ;
	
	/**
	 * Creates a new MatrixMethodPlanner object
	 * 
	 * @param goalToReach
	 *            the goal to reach
	 * @param serviceList
	 *            the list of service
	 * @param servicePropertyList
	 *            the list of service properties
	 * @param virtualMemory
	 *            the virtual memory
	 * @param linkList
	 *            the list of links
	 * @param matrixServProp
	 *            the matrix Services X Effects
	 * @param matrixCondServ
	 *            the matrix Preconditionx X Services
	 */
	public MatrixMethodPlanner(MMGoal goalToReach, ArrayList<Service> serviceList,
			ArrayList<ServiceProperty> servicePropertyList, Memory virtualMemory, ArrayList<PropertyLink> linkList,
			Matrix matrixServProp, Matrix matrixCondServ) {
		
		this.goalToReach = goalToReach.getSuccessCondition();
		this.serviceList = serviceList;
		this.servicePropertyList = servicePropertyList;
		this.virtualMemory = virtualMemory;
		this.linkList = linkList;
		this.matrixServProp = matrixServProp;
		this.matrixCondServ = matrixCondServ;
	}
	
	/**
	 * Builds the plan to reach the goal
	 * 
	 * @return the plan
	 */
	public Couple<Boolean, Plan> buildPlan() {
		// we check to see if the goal is already reached
		// if (this.virtualMemory.checkVirtualMemory(this.goalToReach)) {
		// return new Couple<Boolean, Plan>(true, MatrixMethodPlanningModule.GOAL_ALREADY_ACHIEVED);
		// }
		
		Plan plan = new MatrixMethodPlan();
		
		// we update the goal according to the virtual memory
		this.updateGoal();
		
		ArrayList<ArrayList<Service>> allGeneratedPlans = new ArrayList<ArrayList<Service>>();
		
		boolean need_new_Service = true, plan_done = false, go_back = false, everything_done = false, do_again = false;
		
		int i, j, V[][], Service_index = 0, property_index = 0;
		
		ArrayList<int[][]> possible_Services_list = new ArrayList<int[][]>();
		
		ArrayList<int[][]> precondition_vectors = new ArrayList<int[][]>();
		ArrayList<ArrayList<int[][]>> cloned_preconditions_vectors = new ArrayList<ArrayList<int[][]>>();
		
		ArrayList<Integer> precondition_indexes = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> cloned_precondition_indexes = new ArrayList<ArrayList<Integer>>();
		
		ArrayList<Integer> selected_Service_list = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> cloned_selected_Service_list = new ArrayList<ArrayList<Integer>>();
		
		ArrayList<Service> generated_plan = new ArrayList<Service>();
		ArrayList<ArrayList<Service>> cloned_generated_plans = new ArrayList<ArrayList<Service>>();
		
		ArrayList<ServiceProperty> goal_list = new ArrayList<ServiceProperty>();
		ArrayList<ArrayList<ServiceProperty>> cloned_lists_of_goal = new ArrayList<ArrayList<ServiceProperty>>();
		ArrayList<ServiceProperty> clone_goal_list = new ArrayList<ServiceProperty>();
		
		ArrayList<TableClass> virtualMemory_updates_list = new ArrayList<TableClass>();
		ArrayList<Integer> update_index_list = new ArrayList<Integer>();
		
		char[][] orders = new char[this.servicePropertyList.size()][this.serviceList.size()];
		ArrayList<char[][]> cloned_orders = new ArrayList<char[][]>();
		
		// we create the orders matrix
		for (i = 0; i < this.servicePropertyList.size(); i++) {
			for (j = 0; j < this.serviceList.size(); j++) {
				orders[i][j] = this.matrixCondServ.getP_order()[i][j];
			}
		}
		
		// add the goal to reach in the goal list
		goal_list.add(this.goalToReach.deepDataClone());
		
		// generate first V from the GOAL
		V = new int[1][this.servicePropertyList.size()];
		i = 0;
		for (ServiceProperty propr : this.servicePropertyList) {
			if (this.goalToReach.egal(propr)) {
				V[0][i] = 1;
				precondition_indexes.add(i);
			} else {
				V[0][i] = 0;
			}
			i++;
		}
		// add the generated V in the list of preconditions vector
		precondition_vectors.add(V);
		
		while (!everything_done) {
			// we need to find a service to satisfy the current goal
			if (need_new_Service || go_back) {
				need_new_Service = false;
				// find the Service and add it to the selected_Service_list
				if (go_back == false) {
					int[][] possible_Services = this.multiplyMatrix(V, this.matrixServProp.transposeMatrix());
					
					// if there is no service to satisfy the goal we check the list of links
					if (this.egalZero(possible_Services)) {
						if (this.goalToReach.getConclussion(this.linkList) != null) {
							// get the new goal to reach according to the list of links
							this.goalToReach = this.goalToReach.getConclussion(this.linkList).deepDataClone();
							V = new int[1][this.servicePropertyList.size()];
							i = 0;
							for (ServiceProperty propr : this.servicePropertyList) {
								if (this.goalToReach.egal(propr)) {
									V[0][i] = 1;
								} else {
									V[0][i] = 0;
								}
								i++;
							}
							// we search for a service to satisfy the new goal to reach
							possible_Services = this.multiplyMatrix(V, this.matrixServProp.transposeMatrix());
						}
					}
					// we add the service vector to the list of service vectors
					possible_Services_list.add(possible_Services);
					
					// if we didn't find a service to satisfy the goal, we return a null plan
					if (this.egalZero(possible_Services)) {
						// go_back = true;
						return new Couple<Boolean, Plan>(false, MatrixMethodPlanningModule.NO_PLAN_FOUND);
					}
				}
				
				// if we go back in the planning process to try a different choice, we get the old
				// values of all our variables from the moment we've made a choice
				if (go_back == true) {
					// start deleting from the possible_Services_list
					everything_done = this.deleteFromPossibleServiceList(possible_Services_list, this.virtualMemory,
							virtualMemory_updates_list, update_index_list);
					if (everything_done == true) {
						// return allGeneratedPlans;
						return new Couple<Boolean, Plan>(false, MatrixMethodPlanningModule.NO_PLAN_FOUND);
					}
					
					this.goalToReach = clone_goal_list.get(clone_goal_list.size() - 1);
					clone_goal_list.remove(clone_goal_list.size() - 1);
					
					for (i = 0; i < this.servicePropertyList.size(); i++) {
						for (j = 0; j < this.serviceList.size(); j++) {
							orders[i][j] = cloned_orders.get(cloned_orders.size() - 1)[i][j];
						}
					}
					cloned_orders.remove(cloned_orders.size() - 1);
					
					generated_plan = new ArrayList<Service>();
					generated_plan = cloned_generated_plans.get(cloned_generated_plans.size() - 1);
					
					cloned_generated_plans.remove(cloned_generated_plans.size() - 1);
					
					precondition_indexes = new ArrayList<Integer>();
					precondition_indexes = cloned_precondition_indexes.get(cloned_precondition_indexes.size() - 1);
					
					cloned_precondition_indexes.remove(cloned_precondition_indexes.size() - 1);
					
					precondition_vectors = new ArrayList<int[][]>();
					ArrayList<int[][]> last_el = cloned_preconditions_vectors
							.get(cloned_preconditions_vectors.size() - 1);
					for (i = 0; i < last_el.size(); i++) {
						int[][] el = new int[last_el.get(i).length][last_el.get(i)[0].length];
						for (j = 0; j < el.length; j++) {
							for (int k = 0; k < el[0].length; k++) {
								el[j][k] = last_el.get(i)[j][k];
							}
						}
						precondition_vectors.add(el);
					}
					cloned_preconditions_vectors.remove(cloned_preconditions_vectors.size() - 1);
					
					selected_Service_list = new ArrayList<Integer>();
					for (i = 0; i < cloned_selected_Service_list.get(cloned_selected_Service_list.size() - 1).size(); i++) {
						selected_Service_list.add(cloned_selected_Service_list.get(
								cloned_selected_Service_list.size() - 1).get(i));
					}
					cloned_selected_Service_list.remove(cloned_selected_Service_list.size() - 1);
					
					goal_list = new ArrayList<ServiceProperty>();
					goal_list = cloned_lists_of_goal.get(cloned_lists_of_goal.size() - 1);
					
					cloned_lists_of_goal.remove(cloned_lists_of_goal.size() - 1);
					
					go_back = false;
					plan_done = false;
				}
				
				// we count how many services satisfy the goal to reach
				// TODO : emotion stuff
				int choise = 0;
				for (i = 0; i < possible_Services_list.get(possible_Services_list.size() - 1)[0].length; i++) {
					if (possible_Services_list.get(possible_Services_list.size() - 1)[0][i] == 1) {
						if (choise == 0) {
							Service_index = i;
							possible_Services_list.get(possible_Services_list.size() - 1)[0][i] = 0;
						}
						choise++;
					}
				}
				// if the number of found services is bigger that one then we clone everything
				if (choise > 1) {
					update_index_list.add(virtualMemory_updates_list.size());
					clone_goal_list.add(this.goalToReach);
					this.addOrders(cloned_orders, orders);
					this.addPlan(cloned_generated_plans, generated_plan);
					this.addPreconditionVectors(cloned_preconditions_vectors, precondition_vectors);
					this.addPrecondtionIndexes(cloned_precondition_indexes, precondition_indexes);
					
					this.addPrecondtionIndexes(cloned_selected_Service_list, selected_Service_list);
					
					this.addGoalList(cloned_lists_of_goal, goal_list);
				}
				
				// instantiate the Service using the ServiceProperty
				this.instantiateService(Service_index);
				
				selected_Service_list.add(Service_index);
				// cloned_selected_Service_list.add(Service_index);
				
				// generate new precondition_vector from the selected_Service
				int[][] result = this.getPreconditionVectorForSelectedService(Service_index, this.matrixCondServ);
				
				precondition_vectors.add(result);
				// addElement(cloned_preconditions_vectors, result);
			}
			
			// if we don't need a new service to satisfy the current goal
			if (!need_new_Service) {
				V = new int[1][this.servicePropertyList.size()];
				
				for (i = 0; i < orders.length; i++) {
					if (orders[i][Service_index] == '1') {
						V[0][i] = 1;
						property_index = i;
						precondition_indexes.add(i);
					} else {
						V[0][i] = 0;
					}
					orders[i][Service_index] -= 1;
				}
				// instantiate the precondition using the selected_Service
				this.instantiatePrecondition(property_index, Service_index);
			}
			
			// we check in the virtual memory to see if the current precondition is satisfied
			if (this.virtualMemory.checkVirtualMemory(this.servicePropertyList.get(property_index))) {
				// if it is, we put 0 in the precondition vector and we remove the precondition
				// index from the precondition_indexes list
				this.minusVector(precondition_vectors.get(precondition_vectors.size() - 1), V);
				precondition_indexes.remove(precondition_indexes.size() - 1);
				
				// if the last precondition vector is all equal to 0 we delete it and add that means
				// that all the preconditions for a certain service have been met, so we can the
				// service is a good one and we add it to the generated plan
				while (this.egalZero(precondition_vectors.get(precondition_vectors.size() - 1))) {
					do_again = false;
					precondition_vectors.remove(precondition_vectors.size() - 1);
					
					// the planning process has been successfully finished
					if (precondition_vectors.size() == 0) {
						plan_done = true;
						this.printTheGeneratedPlan(generated_plan, this.virtualMemory);
						this.addPlan(allGeneratedPlans, generated_plan);
						generated_plan = new ArrayList<Service>();
						// System.out.print("\n\n\nNr generated plans: " + allGeneratedPlans.size()
						// + "\n\n");
						// go_back = true;
						
						// break;
						
						// we return the generated plan
						return new Couple<Boolean, Plan>(true, plan);
					}
					
					// we update the virtual memory by applying the effects of the good service
					virtualMemory_updates_list.addAll(this.virtualMemory.updateVirtualMemory(this.serviceList
							.get(selected_Service_list.get(selected_Service_list.size() - 1))));
					
					Service good_Service = this.serviceList
							.get(selected_Service_list.get(selected_Service_list.size() - 1));
					
					// we update the secondary emotions
					update_secondary update = new update_secondary();
					
					@SuppressWarnings("unused")
					int[][] result = update.resultingSecondaryEmotions(update.potentialsecondary(good_Service),
							update.potentialvaluesecondary(this.virtualMemory.getKnowledgeAboutOwner().getEmotions()));
					
					this.addService(generated_plan, good_Service);
					
					// we add the plan element with the good service at the end of the generated
					// plan
					plan.append(new PlanElement(good_Service.deepDataClone(), good_Service.getProviderId()));
					
					for (i = 0; i < this.servicePropertyList.size(); i++) {
						orders[i][selected_Service_list.get(selected_Service_list.size() - 1)] = this.matrixCondServ
								.getP_order()[i][selected_Service_list.get(selected_Service_list.size() - 1)];
					}
					
					selected_Service_list.remove(selected_Service_list.size() - 1);
					
					// we check to see if the current goal is satisfied
					if (this.virtualMemory.checkVirtualMemory(goal_list.get(goal_list.size() - 1))) {
						goal_list.remove(goal_list.size() - 1);
						if (precondition_indexes.size() > 0) {
							precondition_vectors.get(precondition_vectors.size() - 1)[0][precondition_indexes
									.get(precondition_indexes.size() - 1)] = 0;
							precondition_indexes.remove(precondition_indexes.size() - 1);
						}
						
						do_again = false;
						// if the current goal is still not satisfied after using the service, that
						// means that we have to plan again for that goal (for example if out goal
						// is to
						// have the thirst gauge bigger than 80 and after drinking the gauge level
						// is
						// 60, that means that our goal is not completly satisfied and we need to
						// continue the planning precess)
					} else {
						do_again = true;
						this.goalToReach = goal_list.get(goal_list.size() - 1).deepDataClone();
						this.updateGoal();
						
						V = new int[1][this.servicePropertyList.size()];
						i = 0;
						for (ServiceProperty propr : this.servicePropertyList) {
							if (this.goalToReach.egal(propr)) {
								V[0][i] = 1;
							} else {
								V[0][i] = 0;
							}
							i++;
						}
						need_new_Service = true;
						break;
					}
				}
				if (do_again == false) {
					need_new_Service = false;
					if (!plan_done) {
						Service_index = selected_Service_list.get(selected_Service_list.size() - 1);
					}
				}
				// if the precondition is not satisfied according to the virtual memory, we add as a
				// new goal to satisfy that precondition
			} else {
				this.goalToReach = this.servicePropertyList
						.get(precondition_indexes.get(precondition_indexes.size() - 1));
				goal_list.add(this.goalToReach.deepDataClone());
				need_new_Service = true;
			}
		}
		
		// return allGeneratedPlans;
		// we return a null plan; this section should be unreacheable
		return new Couple<Boolean, Plan>(false, MatrixMethodPlanningModule.NO_PLAN_FOUND);
	}
	
	/**
	 * updates the goal using the virtual memory
	 */
	private void updateGoal() {
		for (Param parameter : this.goalToReach.getPropertyParameters()) {
			if (parameter.getFixed().equals("false")) {
				try {
					parameter.setParamValue(String.valueOf(this.virtualMemory.getKnowledgeAboutOwner()
							.getPropertiesContainer().getProperty(parameter.getParamName()).getValue()));
					
				} catch (Exception e) {
					
				}
			}
		}
	}
	
	private boolean deleteFromPossibleServiceList(ArrayList<int[][]> possible_actions_list, Memory virtualMemory,
			ArrayList<TableClass> memory_updates_list, ArrayList<Integer> update_index_list) {
		
		// while (this.egalZero(possible_actions_list.get(possible_actions_list.size() - 1))) {
		// possible_actions_list.remove(possible_actions_list.size() - 1);
		// if (possible_actions_list.isEmpty()) {
		// return true;
		// }
		// }
		//
		// if (!memory_updates_list.isEmpty()) {
		// memory.goBackInTheMemory(memory_updates_list,
		// update_index_list.get(update_index_list.size() - 1));
		// update_index_list.remove(update_index_list.size() - 1);
		// }
		//
		// return possible_actions_list.isEmpty();
		return false;
	}
	
	private void addService(ArrayList<Service> list, Service element) {
		list.add(element.deepDataClone());
	}
	
	private void addGoalList(ArrayList<ArrayList<ServiceProperty>> list, ArrayList<ServiceProperty> element) {
		ArrayList<ServiceProperty> toAdd = new ArrayList<ServiceProperty>();
		for (ServiceProperty propr : element) {
			toAdd.add(propr.deepDataClone());
		}
		list.add(toAdd);
	}
	
	private void addPlan(ArrayList<ArrayList<Service>> list, ArrayList<Service> element) {
		// ArrayList<Action> toAdd = new ArrayList<Action>();
		// for (Iterator<Action> it_i = element.iterator(); it_i.hasNext();) {
		// Action plan_action = it_i.next();
		// toAdd.add(new Action(plan_action.getActionName(), plan_action.getActionParameters(),
		// plan_action
		// .getActionPreconditions(), plan_action.getActionEffectsPlus(),
		// plan_action.getActionEffectsMinus()));
		// }
		// list.add(toAdd);
	}
	
	public void printTheGeneratedPlan(ArrayList<Service> generated_plan, Memory virtualMemory) {
		// if (generated_plan == null) {
		// return;
		// }
		//
		// System.out.println("\n\nGenerated plan is: \n");
		// for (int i = 0; i < generated_plan.size(); i++) {
		// System.out.print(generated_plan.get(i).getActionName() + "   parameters:  ");
		// for (Iterator<Param> it_i = generated_plan.get(i).getActionParameters().iterator();
		// it_i.hasNext();) {
		// Param act_param = it_i.next();
		// System.out.print(act_param.getParamName() + ": " + act_param.getParamValue() + " ;  ");
		// }
		// System.out.println();
		// }
		// System.out.println();
		// System.out.println();
	}
	
	/**
	 * We instantiate the precondition using a certain service
	 * 
	 * @param property_index
	 *            the precondition index
	 * @param service_index
	 *            the service index
	 */
	private void instantiatePrecondition(int property_index, int service_index) {
		for (ServiceProperty property : this.serviceList.get(service_index).getServicePreconditions()) {
			if (this.servicePropertyList.get(property_index).egal(property)) {
				this.servicePropertyList.set(property_index, property);
				break;
			}
		}
	}
	
	/**
	 * We instantiate the service using the memory
	 * 
	 * @param index
	 *            the service index
	 */
	private void instantiateService(int index) {
		boolean found = false;
		
		Service selectedService = this.serviceList.get(index);
		ArrayList<Param> serviceParameterList = new ArrayList<Param>();
		serviceParameterList.addAll(selectedService.getServiceParameters());
		
		for (ServiceProperty property : selectedService.getServicePreconditions()) {
			serviceParameterList.addAll(property.getPropertyParameters());
		}
		for (ServiceProperty property : selectedService.getServiceEffectsPlus()) {
			serviceParameterList.addAll(property.getPropertyParameters());
		}
		for (ServiceProperty property : selectedService.getServiceEffectsMinus()) {
			serviceParameterList.addAll(property.getPropertyParameters());
		}
		
		for (Param parameter : serviceParameterList) {
			if (parameter.getFixed().equals("false")) {
				found = false;
				Param goalParameter = this.goalToReach.getParameter(parameter.getParamName());
				if (goalParameter != null) {
					parameter.setParamValue(goalParameter.getParamValue());
					found = true;
				}
				
				if (!found) {
					if (!parameter.getParamType().equals("3DPoint")) {
						try {
							parameter.setParamValue(String.valueOf(this.virtualMemory.getKnowledgeAboutOwner()
									.getPropertiesContainer().getProperty(parameter.getParamName()).getValue()));
							found = true;
						} catch (Exception e) {
							found = false;
						}
					} else if (parameter.getParamName().equals("position")) {
						parameter.setParamValue(this.virtualMemory.getKnowledgeAboutOwner().getPosition()
								.toStringForXML());
						found = true;
					}
				}
				
				if (found == false && parameter.getParamType().equals("3DPoint")) {
					String findValue = parameter.getFindValue();
					if (!findValue.equals("")) {
						String[] args = findValue.split("__");
						String value = selectedService.getParameter(args[0]).getParamValue();
						parameter.setParamValue(this.virtualMemory.getKnowledgeAbout(Long.parseLong(value))
								.getPosition().toStringForXML());
					}
				}
			} else if (parameter.getParamValue().equals("") && !parameter.getFindValue().equals("")) {
				String findValue = parameter.getFindValue();
				if (findValue.equals("id")) {
					parameter.setParamValue(String.valueOf(selectedService.getProviderId()));
				} else {
					try {
						parameter.setParamValue(String.valueOf(this.virtualMemory.getKnowledgeAboutOwner()
								.getPropertiesContainer().getProperty(findValue).getValue()));
					} catch (Exception e) {
						
					}
				}
			}
		}
	}
	
	private int[][] getPreconditionVectorForSelectedService(int index, Matrix matrixCondServ) {
		int[][] B = new int[1][matrixCondServ.getP_order()[0].length];
		B[0][index] = 1;
		
		return this.multiplyMatrix(B, matrixCondServ.transposeMatrix());
	}
	
	/**
	 * Multiply two matrices
	 * 
	 * @param matrix1
	 *            the first matrix
	 * @param matrix2
	 *            the second matrix
	 * @return the multiplication amtrix
	 */
	public int[][] multiplyMatrix(int[][] matrix1, int[][] matrix2) {
		int m1rows = matrix1.length;
		int m1cols = matrix1[0].length;
		int m2cols = matrix2[0].length;
		
		int[][] result = new int[m1rows][m2cols];
		
		for (int i = 0; i < m1rows; i++) {
			for (int j = 0; j < m2cols; j++) {
				for (int k = 0; k < m1cols; k++) {
					if (matrix1[i][k] != 0) {
						result[i][j] += matrix1[i][k] * matrix2[k][j];
					}
				}
			}
		}
		
		return result;
	}
	
	private void addElement(ArrayList<int[][]> list, int[][] element) {
		int[][] toAdd = new int[element.length][element[0].length];
		for (int i = 0; i < element.length; i++) {
			for (int j = 0; j < element[0].length; j++) {
				toAdd[i][j] = element[i][j];
			}
		}
		
		list.add(toAdd);
	}
	
	private void addOrders(ArrayList<char[][]> list, char[][] element) {
		char[][] toAdd = new char[element.length][element[0].length];
		for (int i = 0; i < element.length; i++) {
			for (int j = 0; j < element[0].length; j++) {
				toAdd[i][j] = element[i][j];
			}
		}
		
		list.add(toAdd);
	}
	
	public boolean egalZero(int[][] matrix) {
		for (int i = 0; i < matrix[0].length; i++) {
			if (matrix[0][i] != 0) {
				return false;
			}
		}
		return true;
	}
	
	public int[][] minusVector(int[][] matrix1, int[][] matrix2) {
		for (int i = 0; i < matrix1[0].length; i++) {
			matrix1[0][i] -= matrix2[0][i];
		}
		return matrix1;
	}
	
	public void printOrders(char[][] orders, String message) {
		// System.out.print("\n\n" + message + "\n\n");
		// for (char[] order : orders) {
		// for (int j = 0; j < orders[0].length; j++) {
		// System.out.print(order[j] + " ");
		// }
		// System.out.println();
		// }
	}
	
	public void printMatrixInt(int[][] matrix, String message) {
		// System.out.print("\n\n" + message + "\n\n");
		// for (int[] element : matrix) {
		// for (int j = 0; j < matrix[0].length; j++) {
		// System.out.print(element[j] + " ");
		// }
		// System.out.println();
		// }
	}
	
	public void addPrecondtionIndexes(ArrayList<ArrayList<Integer>> list, ArrayList<Integer> element) {
		ArrayList<Integer> el_new = new ArrayList<Integer>();
		for (int i = 0; i < element.size(); i++) {
			el_new.add(element.get(i));
		}
		list.add(el_new);
	}
	
	public void addPreconditionVectors(ArrayList<ArrayList<int[][]>> list, ArrayList<int[][]> element) {
		ArrayList<int[][]> new_el = new ArrayList<int[][]>();
		for (int i = 0; i < element.size(); i++) {
			this.addElement(new_el, element.get(i));
		}
		list.add(new_el);
	}
}

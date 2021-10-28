package org.matsim.project;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.api.core.v01.population.Population;
import org.matsim.api.core.v01.population.PopulationWriter;

import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.scenario.ScenarioUtils;

public class CleanNetwork {

	static String INPUT_BASE_DIR = "C:/Users/Robin/git/Padang_v2/scenarios/padang/";

	public static void main(String[] args) {

		Config config = ConfigUtils.createConfig();

		//config.global().setCoordinateSystem("EPSG:25833");
		config.plans().setInputFile(INPUT_BASE_DIR + "output_plans_lämmel_unedited.xml");
		config.network().setInputFile(INPUT_BASE_DIR+"output_network_lämmel_morelanes.xml");

		Scenario scenario = ScenarioUtils.loadScenario(config);

		new PopulationWriter(removeRoutesLeaveSelectedPlan(scenario.getPopulation()))
				.write(INPUT_BASE_DIR + "lämmel_plans_woRoutes.xml");

		
	}

	public static Population removeRoutesLeaveSelectedPlan(Population population) {
		
		Population newPop = PopulationUtils.createPopulation(ConfigUtils.createConfig());
		
		for (Person person : population.getPersons().values()) {
			Plan selectedPlan = person.getSelectedPlan();
			//System.out.println(person.getId());
			
			Person carP = newPop.getFactory().createPerson(person.getId());
			carP.addPlan(selectedPlan);
			
			for (PlanElement pe : selectedPlan.getPlanElements()) {
				if (pe instanceof Leg) {
					((Leg) pe).setRoute(null);
				}
			}
			
			newPop.addPerson(carP);
			
			}
		
		return newPop;
	}

	public static Population removeRoutesLeaveFirstPlan(Population population) {

		for (Person person : population.getPersons().values()) {
			boolean firstPlanHandled = false;
			for (Plan plan : person.getPlans()) {
				// remove all plans except the first
				if (firstPlanHandled) {
					person.removePlan(plan);
				} else {
					// remove route of the first plan
					for (PlanElement pe : plan.getPlanElements()) {
						if (pe instanceof Leg) {
							((Leg) pe).setRoute(null);
						}
					}
					firstPlanHandled = true;
				}
			}
		}
		return population;
	}

	public static Population removeAllPlans(Population population) {

		for (Person person : population.getPersons().values()) {
			for (Plan plan : person.getPlans()) {
				person.removePlan(plan);
			}
		}
		return population;
	}
}

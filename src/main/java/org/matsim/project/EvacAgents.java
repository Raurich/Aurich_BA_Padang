package org.matsim.project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.matsim.api.core.v01.events.PersonArrivalEvent;
import org.matsim.api.core.v01.events.handler.PersonArrivalEventHandler;
import org.matsim.core.config.Config;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;

import com.google.inject.Inject;

public class EvacAgents implements PersonArrivalEventHandler, IterationEndsListener {

	@Inject
	private Config config;
	private int iteration;
	private FileWriter myWriter;
	private double lastArrivalTime;
	private int totalAgentsEvac;
	private boolean firstEvent = true;

	public static void main(String[] args) throws IOException {

	}

	@Override
	public void handleEvent(PersonArrivalEvent event) {

		if (iteration == config.controler().getLastIteration()) {
			if (firstEvent) {
				lastArrivalTime = event.getTime();
				firstEvent = false;
			}
			if (event.getTime() > lastArrivalTime) {

				for (double i = lastArrivalTime; i < event.getTime(); i++) {
					try {
						myWriter.write(i + "\t" + totalAgentsEvac + "\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				lastArrivalTime = event.getTime();
				totalAgentsEvac++;

			} else { // Wenn event.getTime == lastArrivalTime

				totalAgentsEvac++;

			}

		}
	}

	@Override
	public void reset(int iteration) {

		PersonArrivalEventHandler.super.reset(iteration);

		this.iteration = iteration;

		if (iteration == config.controler().getLastIteration()) {

			// File myObj = new File(config.controler().getOutputDirectory());

			try {
				myWriter = new FileWriter(config.controler().getOutputDirectory() + "/EvacAgentsOverTime.txt");
				myWriter.write("SimulationTime\t AgentsEvacuated" + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void notifyIterationEnds(IterationEndsEvent event) {

		if (iteration == config.controler().getLastIteration()) {

			try {
				myWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}

package me.modmuss50.ftba.files.config;

import java.util.List;

/**
 * Created by modmuss50 on 13/03/2017.
 */
public interface IRequirementProvider {

	void getRequirements(List<String> lines, FTBAchievement ftbAchievement);
}

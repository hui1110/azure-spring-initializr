/*
 * Copyright 2012-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azure.spring.initializr.extension.dependency.springazure;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that automatically adds "spring-cloud-azure-starter-actuator" when any Spring Cloud Azure
 * library and Spring Boot Actuator are selected.
 *
 *
 */
public class SpringAzureJdbcBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		if (build.dependencies().has("postgresql")) {
			if (build.dependencies().has("azure-support")) {
				build.dependencies().add("spring-cloud-azure-starter-jdbc-postgresql",
						Dependency.withCoordinates("com.azure.spring", "spring-cloud-azure-starter-jdbc-postgresql"));
			}
		}
		if (build.dependencies().has("mysql")) {
			if (build.dependencies().has("azure-support")) {
				build.dependencies().add("spring-cloud-azure-starter-jdbc-mysql",
						Dependency.withCoordinates("com.azure.spring", "spring-cloud-azure-starter-jdbc-mysql"));
			}
		}
	}

}

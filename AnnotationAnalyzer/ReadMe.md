# How to use the code to extract and analyze annotations?

  - In the ***config.properties*** file, change the value of ***appsDir***. The value should be a directory's path that contains apps to be analyzed.
  - In order to perform analysis on our dataset, change the value of ***downloadExperimentalDataset*** to ***true*** in the ***config.properties*** file. The code will clone apps from GitHub in the directory specified in ***appsDir*** and then perform the analysis. You must be logged in to your GitHub account.
  
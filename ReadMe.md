# Annotation practices in Android apps

Existing studies indicate that Java annotations are widely used by developers. However, there is currently no empirical data on annotation usage in Android apps. Android apps are often smaller than general Java applications and typically use Android APIs or specific libraries catered to the mobile environment. Therefore, it is not clear if the results of existing Java studies hold for Android apps. Therefore, we investigate annotation practices in Android apps through an empirical study of 1,141 open-source apps. Using previously studied metrics, we first compare annotation usage in Android apps to existing results from general Java applications. Then, for the first time, we study why developers declare custom annotations.

This repository contains the source code and data set of our study on annotation practices in Android apps. The study has been published in Proceedings of 20th IEEE International Working Conference on Source Code Analysis & Manipulation (SCAM), 2020.  

## Contents

  - The Project Metadata CSV file includes descriptive statistics on the studied apps, such as project name, GitHub and Play store links, specific GitHub commits used in this study, project size in LOC, etc.
  - The folder AnnotationUsage contains data on annotation usage, such as the number of annotations per app, per LOC for each file, and per annotated program element.
  - The folder Metrics contains data on seven different annotation metrics defined by Lima et al.
  - The folder AnnotationTypes contains data on different types of annotations and their frequency in the studied apps. It also includes data on qualitative study of custom annotations.
  - The folder AnnotationAnalyzer contains source code used to clone the studied apps from the GitHub and extract and analyze annotations from the apps. The details about how to use the code are given in the included ReadMe file inside AnnotationAnalyzer folder.

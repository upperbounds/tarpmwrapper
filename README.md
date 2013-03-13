
* build the jar file: ./sbt package
* copy the jar to the CQ deployment: cp target/scala-2.10/tarpmwrapper_2.10-1.0.jar $CQ_HOME/crx-quickstart/server/runtime/0/_crx/WEB-INF/lib
* update the logging properties in $CQ_HOME/crx-quickstart/server/runtime/0/_crx/WEB-INF/log4j.xml (see example in config dir)                                                                                                                                   1
* update the workspace config to use the new PM in $CQ_HOME/crx-quickstart/repository/workspaces/crx.default/workspace.xml (see example in config dir)

# vcs-query-tool
Framework written in #Java which eases management of multiple VCS.


Progress log:
For deserializing responses into POJO DTOs, I should be able to Map the JSON's fields as a HashMap<String, String>. Looking for a standard way to achieve this.

Started a Gradle project. At first glance, the structure is better.

Will have to copy all files from the old monolith to the Gradle project. Tests are restructured on //fs but have the same logic (code).
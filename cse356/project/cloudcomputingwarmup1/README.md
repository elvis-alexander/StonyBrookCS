# cloudcomputingwarmup1


Step 1: Create a front page at http://yourserver/eliza/ – the page must include at least one CSS file which changes the appearance of something on the page and a POST form that requests and submits a field called “name” (the FORM action should point to this page’s own URL).<br/><br/>
Step 2: If the page receives a POST parameter called “name”, it should output “Hello $name, $date” with the name and date filled in dynamically. (do not use client-side JavaScript for this part)<br/>
Step 3: Create a REST-based ELIZA service at http://yourserver/eliza/DOCTOR that takes as input a JSON object including a “human” property and receives back a JSON object including an “eliza” property, each containing the corresponding next phrase of the therapy session.<br/>
Step 4: Integrate the REST-based ELIZA service into your front page that starts operating when the page is loaded with a “name” specified. (use client-side JavaScript for this part)<br/>


Run with nodejs
```
nodejs app.js
```

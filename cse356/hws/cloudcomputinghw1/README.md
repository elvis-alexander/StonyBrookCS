
# Elvis Fernandez

Step 1: Place your HW#0 files into a public git repository (use a service such as github or bitbucket)<br>
Step 2: Create an Ansible playbook to deploy your HW#0 on Ubuntu 16.04 servers, checking out the files from git and using “hw1” as the name for hosts: in your inventory<br>
Step 3: Place your playbook at http://yourserver/hw1.yml


Install Ansible
```
$ sudo apt-get install software-properties-common
$ sudo apt-add-repository ppa:ansible/ansible
$ sudo apt-get update
$ sudo apt-get install ansible
```


Clone Repo + Run playbook
```
git clone https://github.com/elvis-alexander/cloudcomputinghw1.git
ansible-playbook ./cloudcomputinghw1/hw1.yml
```
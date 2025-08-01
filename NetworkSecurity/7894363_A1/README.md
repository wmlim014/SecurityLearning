# Student Information
Student Name: Lim Wen Mi<br/>
UOW ID: 7894363<br/>
Submission For: CSCI368 Assignment 1<br/>
*All the references for each stage are listed as command line within each Program files*<br/>
**GitHub: https://github.com/wmlim014/SecurityLearning/tree/main/NetworkSecurity/7894363_A1**

# Steps for Program Execution in DEES VM Machine
## JDK Installation Guide (If Required)
```
sudo apt update
```
```
sudo apt install default-jdk
```

## Mount VM Shared Folder
```
mkdir -p ~/Share
sudo mount -t vboxsf VM_Shared ~/Share
```

## Before Execution...
1. Unzip and allocate the submitted folder (`7894363_A1`) into share VM_Share folder.
2. Open a terminal and redirect the dictionary into program folder: 
```
cd /home/seed/Share/7894363_A1
```

## Start Execution
## Create Java Class
```
javac src/Setup.java src/Alice/Host.java src/Bob/Client.java src/Crypto/RC4.java src/Alice/User.java
```

### Setup of Host
Generate the Diffie-Hellman parameters (p, g), choose a password PW for Bob and save (p, g, H(PW)) in 
a text file named `pwSheets.txt` under the directory of Alice.
> [!NOTE]
> 1. p = random Prime number generated between 1 to 100
> 2. g = a primitive root of `p` (*random int in range 1 < g < p*)
> 3. H(PW) = hashed string for selected password (*Please input the password you choose*)
> 4. Selected Password = required contains at least 6 alphanumeric characters characters (*(A-Z, both uppercase and lowercase) and numbers (0-9)*)

*<ins>You could need to update `line 20` of file <b>Setup.java</b> to `Alice/pwSheets.txt` for linux VM machine</ins>*
```
java -cp src Setup
```
**The file will be stored as the following information: <ins>Username, p, g, H(PW)</ins>**

### Host and Client Execution
> [!NOTE]
> Need to open two terminals, a terminal for Host, another for Client<br/>
> Selected port = 363<br/>
> *If you like to change the port can change the `363` in `PORT` variable from both `Host.java (line 19)` and `Client.java (line 10)` to the port you selected. But both port <ins>must change to same value</ins>.*
> Reference: https://www.geeksforgeeks.org/java/socket-programming-in-java/
**HOST TERMINAL WINDOW**
```
java -cp src Alice.Host
```

**CLIENT TERMINAL WINDOW**
> **To Terminate Host Connection:** type `exit`<br/>
```
java -cp src Bob.Client
```

# APPENDIX: Default Documentation From VSCode in Window Host Machine
## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
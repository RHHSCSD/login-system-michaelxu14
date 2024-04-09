/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package loginsystem;

/**
 *
 * @author michael.roy-diclemen
 */
public class LoginSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RegistrationSystem registrationSystem = new RegistrationSystem();

        
        LoginFrame loginFrame = new LoginFrame(registrationSystem);
        
        
        //Comment when not testing
        //registrationSystem.clearFile();
        RegisterFrame registerFrame = new RegisterFrame(registrationSystem);
        HubFrame hubFrame = new HubFrame(registrationSystem);
        

  
        hubFrame.setVisible(true); // Show the hub frame
    }
    
}

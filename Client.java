package chatting.application;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.net.*; //for server socket class
import java.io.*; //for datainput stream

public class Server implements ActionListener
{
    JTextField text; //writing messages. 
    JPanel a1;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();
    static DataOutputStream dout;
    
    Server() //constructor
    {
        f.setLayout(null); //for setting components like images, texts etc;
        //set to null for manually inputting layout locations. 
        
        JPanel p1 = new JPanel(); //setting panels on the frame. 
        p1.setBackground(new Color(7, 94, 84)); //red, green, blue(rgb format). 
        p1.setBounds(0, 0, 450, 55);
        
        p1.setLayout(null);
        
        f.add(p1);
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/3.png")); //for setting images from file directives. 
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT); //for scaling the iamge. 
        ImageIcon i3 = new ImageIcon(i2); //back button image
        JLabel back = new JLabel(i3);
        back.setBounds(5, 15, 20, 20);       
        
        p1.add(back); //for adding images on the panel. 
        
        back.addMouseListener(new MouseAdapter() //action needed when clicking back button. 
        {
            public void mouseClicked(MouseEvent ae)
            {
                System.exit(0); //when clicked, application will be closed. 
            }
        });
        
        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/1.png"));       
        Image i5 = i4.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);       
        ImageIcon i6 = new ImageIcon(i5); //profile image
        JLabel profile = new JLabel(i6);
        profile.setBounds(35, 7, 40, 40);     
        p1.add(profile);
        
        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));       
        Image i8 = i7.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);       
        ImageIcon i9 = new ImageIcon(i8); //video image
        JLabel video = new JLabel(i9);
        video.setBounds(300, 10, 30, 30);     
        p1.add(video);
        
        ImageIcon i10 = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));       
        Image i11 = i10.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);       
        ImageIcon i12 = new ImageIcon(i11); //call image  
        JLabel phone = new JLabel(i12);
        phone.setBounds(350, 10, 30, 30);     
        p1.add(phone);
        
        ImageIcon i13 = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));       
        Image i14 = i13.getImage().getScaledInstance(10, 30, Image.SCALE_DEFAULT);       
        ImageIcon i15 = new ImageIcon(i14); //more option image
        JLabel more = new JLabel(i15);
        more.setBounds(400, 10, 10, 30);     
        p1.add(more);
        
        JLabel name = new JLabel("Bitan"); //user name
        name.setBounds(85, 15, 100, 15);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 15));
        p1.add(name);
        
        JLabel status = new JLabel("Active Now"); //active status
        status.setBounds(85, 35, 100, 10);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.BOLD, 10));
        p1.add(status);
        
        a1 = new JPanel(); //text area
        a1.setBounds(5, 60, 440, 500);
        f.add(a1);
        
        text = new JTextField(); //typing messages
        text.setBounds(5, 561, 310, 40);
        text.setFont(new Font("ARIEL", Font.PLAIN, 15));
        f.add(text);
        
        JButton send = new JButton("Send"); //send button
        send.setBounds(306, 561, 138, 40);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        send.addActionListener(this); //action performed on clicking send
        send.setFont(new Font("ARIEL", Font.PLAIN, 15));
        f.add(send);
        
        f.setSize(450, 600); //setting the size of the frame
        f.setLocation(200, 50); //setting the location of the frame on screen
        f.setUndecorated(true); //for removing frame header. 
        f.getContentPane().setBackground(Color.WHITE); //background colour
        
        f.setVisible(true); //visibility generally remains hidden in default
    }
    
    public void actionPerformed(ActionEvent ae) //overwriting unimplemented method in ActionListener interface. 
    {
        try
        {
            String out = text.getText(); //to get the text from text-field
        
            JPanel p2 = formatLabel(out);

            a1.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout()); 
            right.add(p2, BorderLayout.LINE_END);

            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            a1.add(vertical, BorderLayout.PAGE_START);

            dout.writeUTF(out);

            text.setText("");

            f.repaint();
            f.invalidate();
            f.validate();
        }
        
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static JPanel formatLabel(String out)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        
        output.setBorder(new EmptyBorder(15, 15, 15, 50));
        
        panel.add(output);
        
        Calendar cal = Calendar.getInstance(); //timestamp
        SimpleDateFormat sdf =  new SimpleDateFormat("HH:mm");
        
        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        
        panel.add(time);
        
        return panel;
    }
    
    public static void main(String[] args)
    {
        new Server(); //creating annonymous object of the class
        
        try
        {
            ServerSocket skt = new ServerSocket(6001); //port number
            while(true)
            {
                Socket s = skt.accept(); //for accepting all messages
                DataInputStream din = new DataInputStream(s.getInputStream());
                dout = new DataOutputStream(s.getOutputStream());
                
                while(true)
                {
                    String msg = din.readUTF();
                    JPanel panel = formatLabel(msg);
                    
                    JPanel left = new JPanel(new BorderLayout()); //received messages shown at left
                    left.add(panel, BorderLayout.LINE_START); 
                    vertical.add(left);
                    f.validate();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
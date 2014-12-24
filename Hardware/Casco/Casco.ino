
const int InputFreq =  A0; 
int balas = 0;
int VoltajeIN = 0;
int stat = LOW;
int stat2;
long last = 0;


int ledPin = 13;
char comenzar = '0';
char dentrociclo = '0';

void setup() {
  // initialize serial:
  Serial.begin(9600);
  pinMode(ledPin, OUTPUT);
}

void loop() 
{
  // if there's any serial available, read it:  
  while (Serial.available() > 0) 
  {
    
    comenzar = Serial.read(); 
    Serial.print("~");
    delay(20);
    if(comenzar == '*')
    {
      do
      {
        
        /*
        delay(5);
        dentrociclo = Serial.read();
        if(dentrociclo == 'B' )
        {
           Serial.print("S");     
        }
        else
        {
          Serial.print("N");
          break;
        }
        */
           
           
    
         VoltajeIN = analogRead(InputFreq);
         if(VoltajeIN < 400)
           stat = LOW;
         else
           stat = HIGH;
     
         //digitalWrite(13,stat);
     
         if(stat2!=stat)
         {
           balas++;
           stat2=stat;
         }
    
         if(millis()-last >= 250)
         {
       
           Serial.print(balas);
       
           balas = 0;
           last = millis();
      
           if(Serial.read() == 1)
           {
             digitalWrite(ledPin, HIGH); 
           }
           else
           {
             digitalWrite(ledPin,LOW);
           }
           
         } 
       
      }while(1);   
    }  
  }
}

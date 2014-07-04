int BtnGatillo = 13;  // Pin para boton del gatillo
int BtnCargador = 12;  // Pin para boton del cargador

int EstadoBtnCActual; //Estado del boton del cargador actual
byte EstadoBtnCAnterior; //Estado del boton del cargador anterior

int EstadoBtnGActual; //Estado del boton del gatillo actual
byte EstadoBtnGAnterior; //Estado del boton del cargador anterior

unsigned long MilisAnteriores = 0; // last time update
long TiempoCargador = 4000; //Tiene que estar presionado el boton cargador este tiempo
long TiempoDisparo = 200; //Tiempo en que esta disparando

unsigned long MilisActuales = 0; //Milisegundos Actuales

int BalasCartuchos = 30; //Balas que se encuentran en los cargadores
int BalasActuales = 30; //Balas que pueden ser disparadas

int LaserBalas = 8; //Pin para el led de las balas
int Vibrador = 9; //Pin para el vibrador

int MantenerDisparo = 0;//Variable para mantener disparo
int MantenerVibrado = 0;//Variable para mantener vibrado

int MilisDisparoAnteriores = 0;
int MilisDisparo = 0;

void setup()
{
  Serial.begin(9600);    // Use serial for debugging
 
  digitalWrite(BtnGatillo, HIGH); 
  digitalWrite(BtnCargador, HIGH); 

  pinMode(LaserBalas, OUTPUT);
  pinMode(Vibrador, OUTPUT);
}

void loop()
{
  if(MantenerDisparo == 1 && MantenerVibrado == 1)
  {
    MilisDisparo = millis();
    if(MilisDisparo - MilisDisparoAnteriores > TiempoDisparo)
    {
      MilisDisparoAnteriores = MilisDisparo;
      digitalWrite(LaserBalas, LOW); 
      MantenerDisparo = 0;
      
      digitalWrite(Vibrador, LOW); 
      MantenerVibrado = 0;
    }
    else
    {
      digitalWrite(LaserBalas, HIGH);
      digitalWrite(Vibrador, HIGH);
    }
  }
  else
  {
    PresionaCargador();
    PresionaGatillo();

    MilisDisparo = millis();
    MilisDisparoAnteriores = MilisDisparo;
  } 
  //Se pasan los estados actuales a anteriores
  EstadoBtnGAnterior = EstadoBtnGActual; //Gatillo
  EstadoBtnCAnterior = EstadoBtnCActual; //Cargador
}

void Vibrar()
{  
  MantenerVibrado = 1; 
}

void Disparar()
{
  MantenerDisparo = 1;
  BalasActuales--;
}

void Cargar()
{
  for(int x = BalasActuales; x < 30 ; x++ )
  {
    BalasCartuchos--;
    BalasActuales++;
  }
}

void PresionaGatillo()
{
  //Se lee el boton del gatillo
  EstadoBtnGActual = digitalRead(BtnGatillo);
  
  if(EstadoBtnGActual == HIGH && EstadoBtnGAnterior == LOW)
  { 
    if(BalasActuales > 0)
    {
      Vibrar();
      Disparar();
      //delay(1000);
      ImprimirEnDisplay();
    }
  }
}

void PresionaCargador()
{
  //Se lee el boton del cargador
  EstadoBtnCActual = digitalRead(BtnCargador);
  //Si NO esta presionado el boton(cargador)
  if( EstadoBtnCActual == LOW)
  { 
    MilisActuales = millis();
    MilisAnteriores = MilisActuales;
  }
  else
  {
    MilisActuales = millis();       
    if(MilisActuales - MilisAnteriores > TiempoCargador) 
    {
       MilisAnteriores = MilisActuales;  
       if(BalasCartuchos <= 0)
       {
         Serial.println("Sin balas");
       }
       else
       {
         Cargar();
         Serial.print("Cargo");
       }
    }
  }
}

void ImprimirEnDisplay()
{
  Serial.print(BalasCartuchos);
  Serial.print(" // ");
  if(BalasActuales > 0)
    Serial.println(BalasActuales);
  else
    Serial.println("Recarga");
}

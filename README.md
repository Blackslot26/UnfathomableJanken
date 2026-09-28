# UnfathomableJanken
Juego táctico de combate elemental por turnos basado en una mecánica de piedra, papel o tijera (Janken) de 5 elementos.

## Gameplay
- Cada jugador cuenta con un mazo de elementos activos.
- Cada elemento posee energia, fortalezas y debilidades. Este puede ser jugado mientras que su energia sea mayor a 0.
- Cuando un elemento es derrotado el jugador puede intercambiarlo por uno nuevo.
- La partida finaliza cuando todos los elementos de un jugador fueron derrotados.

## Matriz de daños
Cada elemento cuenta con una distribución simétrica e inversa: un counter mayor (60), un counter menor (40), un impacto neutral frente a sí mismo (35) y dos desventajas defensivas (30 y 20).
```===================================================================
                  MATRIZ DE DAÑO ELEMENTAL
======================================================================
 ATACANTE \ DEFENSOR |  Fuego  |  Agua   |  Tierra |  Madera |  Metal  
 --------------------+---------+---------+---------+---------+------- 
 Fuego               |   35    |   20    |   30    |   40    |   60    
 Agua                |   60    |   35    |   20    |   30    |   40    
 Tierra              |   40    |   60    |   35    |   20    |   30    
 Madera              |   30    |   40    |   60    |   35    |   20    
 Metal               |   20    |   30    |   40    |   60    |   35    
======================================================================
```

## Tipos de oponentes (AI)
- **RandomAI (Easy mode)**: Selecciona cartas de forma puramente aleatoria sin evaluar el estado del tablero.
- **StrategicAI (Normal mode)**: Evalúa el elemento activo del rival y juega directamente el ataque de mayor daño disponible en su mano.
- **SuperAI (Hard mode)**: IA táctica de 2 fases. 
  - En el primer turno prioriza seleccionar el elemento de menor valor general en contra del rival. 
  - En sus turnos prioriza rematar objetivos heridos consumiendo la menor utilidad residual posible, reservando sus elementos clave para turnos posteriores.
  - Cuando no es posible realizar un remate, elije el elemento con mayor daño y menor utilidad.
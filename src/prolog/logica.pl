

listarInventario(Resultado):-findall((Valor,Z),(inventario(Valor,Z)),Resultado). %prueba listar lo q esta en inventario.

listarIngredientes(Objeto,Resultado):-findall((Valor,Z),(ingrediente(Objeto,Valor,Z)),Resultado).  %listo los ingredientes de un objeto

listaroxido(Resultado):-findall((Ingrediente,Z),(ingrediente(oxido,Ingrediente,Z)),Resultado). % prueba listando de oxido.

%devuelve true o false // del ingrediente hay cantidad suficiente en el inventario?
tiene_suficiente(Ingrediente, Cantidad) :-
    inventario(Ingrediente, CantidadInventario),
    Cantidad =< CantidadInventario.

%Devuelve true o false
suficiente(Lista) :-
    forall(
        member((Ingrediente, Cantidad), Lista),  % Itera sobre cada (Ingrediente, Cantidad)
        tiene_suficiente(Ingrediente, Cantidad)   % Valida que haya suficiente
    ).

posibleCrafteo_OLD(Objeto):-
    ingredientecompuesto(Objeto),    %Es objeto compuesto.
    listarIngredientes(Objeto,Resultado),  %listar los  ingredientes del objeto
    suficiente(Resultado).       % compruebo si hay suficientes.

%mesas en el inventario
mesas_inv(Objeto):- no_apilable(Objeto), inventario(Objeto, _).

posibleCrafteo(Objeto):-
    ingredientecompuesto(Objeto), \+mesas_inv(Objeto),   %Es objeto compuesto.
    listarIngredientes(Objeto,Resultado),  %listar los  ingredientes del objeto
    suficiente(Resultado).       % compruebo si hay suficientes.

%Prueba
%posibleCrafteo(Objeto)
%resultado (oxido y clavo)
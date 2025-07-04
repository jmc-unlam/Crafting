% Recetas
% Lista de Objetos Crafteables 
ingredientecompuesto('lingote de acero').
ingredientecompuesto('lingote de hierro').
ingredientecompuesto('lingote de estano').
ingredientecompuesto('Mesa de Flechas').
ingredientecompuesto('lingote de bronze').
ingredientecompuesto('lingote de plata').
ingredientecompuesto('lingote de oro').
ingredientecompuesto('lingote de cobre').
ingredientecompuesto('Mesa Generica').
ingredientecompuesto('Punta de Flecha').

% Ingredientes por objeto
ingrediente('lingote de acero', 'lingote de hierro', 2).
ingrediente('lingote de acero', 'carbon', 8).
ingrediente('lingote de hierro', 'mineral de hierro', 7).
ingrediente('lingote de estano', 'mineral de estano', 4).
ingrediente('Mesa de Flechas', 'madera', 10).
ingrediente('Mesa de Flechas', 'ladrillo', 20).
ingrediente('lingote de bronze', 'lingote de estano', 1).
ingrediente('lingote de bronze', 'lingote de cobre', 4).
ingrediente('lingote de plata', 'mineral de plata', 3).
ingrediente('lingote de oro', 'mineral de oro', 3).
ingrediente('lingote de cobre', 'mineral de cobre', 3).
ingrediente('Mesa Generica', 'madera', 10).
ingrediente('Mesa Generica', 'ladrillo', 20).
ingrediente('Punta de Flecha', 'lingote de hierro', 1).

% Mesas
no_apilable('Mesa de Flechas').
no_apilable('Mesa Generica').
no_apilable('Mesa Default').

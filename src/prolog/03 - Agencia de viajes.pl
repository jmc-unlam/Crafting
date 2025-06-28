transporte(roma, 20).
transporte(londres, 30).
transporte(tunez, 10).

alojamiento(roma, hotel, 50).
alojamiento(roma, hostal, 30).
alojamiento(roma, camping, 10).
alojamiento(londres, hotel, 60).
alojamiento(londres, hostal, 40).
alojamiento(londres, camping, 20).
alojamiento(tunez, hotel, 40).
alojamiento(tunez, hostal, 20).
alojamiento(tunez, camping, 5).

viaje(Ciudad, Semanas, Hospedaje, Precio):-
    transporte(Ciudad, PrecioTransporte),
    alojamiento(Ciudad, Hospedaje, PrecioSemanalHospedaje),
    Precio is PrecioTransporte + PrecioSemanalHospedaje * Semanas.


viajeeconomico(Ciudad, Semanas, Hospedaje, Precio, PrecioMinimo):-
    viaje(Ciudad, Semanas, Hospedaje, Precio),
    Precio =< PrecioMinimo.


viaje2(Ciudad, Semanas, Hospedaje, Precio):-
    transporte(Ciudad, PrecioTransporte),
    alojamiento(Ciudad, Hospedaje, PrecioSemanalHospedaje),
    (
    	(
        	\+var(Semanas),
            Precio is PrecioTransporte + PrecioSemanalHospedaje * Semanas
        );
        (
        	var(Semanas),
            Precio = PrecioTransporte + PrecioSemanalHospedaje * Semanas
        )
    ).


:- use_module(library(clpfd)).

viaje3(Ciudad, Semanas, Hospedaje, Precio):-
    transporte(Ciudad, PrecioTransporte),
    alojamiento(Ciudad, Hospedaje, PrecioSemanalHospedaje),
    PrecioTransporte + PrecioSemanalHospedaje * Semanas #= Precio.

% Permite no solo usar semanas como variable, sino también definir un precio total
% viaje3(Ciudad, Semanas, Hospedaje, 100)
% roma	8	camping
% tunez	18	camping

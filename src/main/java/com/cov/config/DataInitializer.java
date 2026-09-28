package com.cov.config;

import com.cov.enums.StatutReclamation;
import com.cov.enums.StatutReservation;
import com.cov.enums.StatutTrajet;
import com.cov.enums.TypeTrajet;
import com.cov.model.Admin;
import com.cov.model.Avis;
import com.cov.model.Conducteur;
import com.cov.model.Reclamation;
import com.cov.model.Reservation;
import com.cov.model.Trajet;
import com.cov.model.Vehicule;
import com.cov.model.Voyageur;
import com.cov.repository.AvisRepository;
import com.cov.repository.ReclamationRepository;
import com.cov.repository.ReservationRepository;
import com.cov.repository.TrajetRepository;
import com.cov.repository.UtilisateurRepository;
import com.cov.repository.VehiculeRepository;
import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedDemoData(UtilisateurRepository utilisateurRepository,
                                   VehiculeRepository vehiculeRepository,
                                   TrajetRepository trajetRepository,
                                   ReservationRepository reservationRepository,
                                   AvisRepository avisRepository,
                                   ReclamationRepository reclamationRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            seedAdmin(utilisateurRepository, passwordEncoder);
            seedTunisiaDemoData(
                    utilisateurRepository,
                    vehiculeRepository,
                    trajetRepository,
                    reservationRepository,
                    avisRepository,
                    reclamationRepository,
                    passwordEncoder
            );

            if (utilisateurRepository.findByEmail("conducteur.amine@cov.local").isPresent()) {
                return;
            }

            String encodedPassword = passwordEncoder.encode("demo123");

            Conducteur amine = utilisateurRepository.save(new Conducteur(
                    "Bennani",
                    "Amine",
                    "conducteur.amine@cov.local",
                    encodedPassword,
                    "0611000001",
                    "B-123456"
            ));
            amine.setNote(4.8);
            utilisateurRepository.save(amine);

            Conducteur sara = utilisateurRepository.save(new Conducteur(
                    "El Mansouri",
                    "Sara",
                    "conducteur.sara@cov.local",
                    encodedPassword,
                    "0611000002",
                    "B-987654"
            ));
            sara.setNote(4.6);
            utilisateurRepository.save(sara);

            Voyageur lina = utilisateurRepository.save(new Voyageur(
                    "Alaoui",
                    "Lina",
                    "voyageur.lina@cov.local",
                    encodedPassword,
                    "0622000001"
            ));
            Voyageur yassine = utilisateurRepository.save(new Voyageur(
                    "Karimi",
                    "Yassine",
                    "voyageur.yassine@cov.local",
                    encodedPassword,
                    "0622000002"
            ));

            Vehicule clio = vehiculeRepository.save(vehicle(
                    amine,
                    "Renault",
                    "Clio",
                    "Citadine",
                    "COV-101-A",
                    4,
                    "Bleu",
                    2022,
                    "/uploads/vehicules/1647c9a2-2dbd-47d6-a240-79c20cc0fa44_Screenshot 2026-05-06 091525.png"
            ));
            Vehicule dacia = vehiculeRepository.save(vehicle(
                    sara,
                    "Dacia",
                    "Duster",
                    "SUV",
                    "COV-202-S",
                    5,
                    "Gris",
                    2021,
                    "/uploads/vehicules/a5b7f2a1-70e6-4a22-b48b-6e0b2d764d80_WhatsApp Image 2026-01-12 at 11.37.22 AM.jpeg"
            ));

            Trajet parisLyon = trajetRepository.save(trip(
                    amine,
                    clio,
                    "Paris",
                    "Lyon",
                    LocalDateTime.now().plusDays(3).withHour(8).withMinute(30).withSecond(0).withNano(0),
                    3,
                    2,
                    35.0,
                    465,
                    TypeTrajet.LONG,
                    false,
                    true,
                    1,
                    "Cabine",
                    StatutTrajet.OUVERT
            ));
            Trajet rabatCasa = trajetRepository.save(trip(
                    sara,
                    dacia,
                    "Rabat",
                    "Casablanca",
                    LocalDateTime.now().plusDays(1).withHour(18).withMinute(0).withSecond(0).withNano(0),
                    4,
                    3,
                    7.0,
                    90,
                    TypeTrajet.LEGER,
                    false,
                    false,
                    2,
                    "Moyen",
                    StatutTrajet.OUVERT
            ));
            Trajet tangerFes = trajetRepository.save(trip(
                    sara,
                    dacia,
                    "Tanger",
                    "Fes",
                    LocalDateTime.now().plusDays(5).withHour(9).withMinute(15).withSecond(0).withNano(0),
                    4,
                    4,
                    22.0,
                    395,
                    TypeTrajet.LONG,
                    true,
                    false,
                    1,
                    "Petit",
                    StatutTrajet.OUVERT
            ));
            trajetRepository.save(trip(
                    amine,
                    clio,
                    "Marrakech",
                    "Agadir",
                    LocalDateTime.now().plusDays(7).withHour(7).withMinute(45).withSecond(0).withNano(0),
                    3,
                    3,
                    18.0,
                    250,
                    TypeTrajet.LONG,
                    false,
                    false,
                    1,
                    "Cabine",
                    StatutTrajet.OUVERT
            ));

            Reservation reservationLina = reservationRepository.save(reservation(
                    lina,
                    parisLyon,
                    1,
                    StatutReservation.CONFIRMEE
            ));
            Reservation reservationYassine = reservationRepository.save(reservation(
                    yassine,
                    rabatCasa,
                    1,
                    StatutReservation.EN_ATTENTE
            ));

            avisRepository.save(review(lina, parisLyon, amine, 5, "Conducteur ponctuel et trajet tres confortable."));
            avisRepository.save(review(yassine, tangerFes, sara, 4, "Bonne communication avant le depart."));

            reclamationRepository.save(reclamation(
                    yassine,
                    reservationYassine,
                    "Demande de confirmation",
                    "Je souhaite savoir si le conducteur peut confirmer rapidement la reservation.",
                    StatutReclamation.OUVERTE
            ));
            reclamationRepository.save(reclamation(
                    lina,
                    reservationLina,
                    "Question bagage",
                    "Le trajet s'est bien passe, je signale seulement un doute sur la taille de bagage annoncee.",
                    StatutReclamation.EN_COURS
            ));
        };
    }

    private void seedAdmin(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        if (utilisateurRepository.findByEmail("admin@cov.local").isEmpty()) {
            Admin admin = new Admin(
                    "Admin",
                    "System",
                    "admin@cov.local",
                    passwordEncoder.encode("admin123"),
                    "0000000000"
            );
            utilisateurRepository.save(admin);
        }
    }

    private void seedTunisiaDemoData(UtilisateurRepository utilisateurRepository,
                                     VehiculeRepository vehiculeRepository,
                                     TrajetRepository trajetRepository,
                                     ReservationRepository reservationRepository,
                                     AvisRepository avisRepository,
                                     ReclamationRepository reclamationRepository,
                                     PasswordEncoder passwordEncoder) {
        if (utilisateurRepository.findByEmail("conducteur.tarek@cov.local").isPresent()) {
            return;
        }

        String encodedPassword = passwordEncoder.encode("demo123");

        Conducteur tarek = utilisateurRepository.save(new Conducteur(
                "Ben Salah",
                "Tarek",
                "conducteur.tarek@cov.local",
                encodedPassword,
                "22111001",
                "TN-A-458921"
        ));
        tarek.setNote(4.9);
        utilisateurRepository.save(tarek);

        Conducteur nour = utilisateurRepository.save(new Conducteur(
                "Trabelsi",
                "Nour",
                "conducteur.nour@cov.local",
                encodedPassword,
                "22111002",
                "TN-B-781245"
        ));
        nour.setNote(4.7);
        utilisateurRepository.save(nour);

        Voyageur ines = utilisateurRepository.save(new Voyageur(
                "Mansour",
                "Ines",
                "voyageur.ines@cov.local",
                encodedPassword,
                "22333001"
        ));
        Voyageur malek = utilisateurRepository.save(new Voyageur(
                "Jaziri",
                "Malek",
                "voyageur.malek@cov.local",
                encodedPassword,
                "22333002"
        ));
        Voyageur salma = utilisateurRepository.save(new Voyageur(
                "Gharbi",
                "Salma",
                "voyageur.salma@cov.local",
                encodedPassword,
                "22333003"
        ));

        Vehicule corolla = vehiculeRepository.save(vehicle(
                tarek,
                "Toyota",
                "Corolla",
                "Berline",
                "TU-2026-101",
                4,
                "Blanc",
                2023,
                "/uploads/vehicules/demo-tunisie-corolla.jpg"
        ));
        Vehicule tucson = vehiculeRepository.save(vehicle(
                nour,
                "Hyundai",
                "Tucson",
                "SUV",
                "TU-2026-202",
                5,
                "Noir",
                2022,
                "/uploads/vehicules/demo-tunisie-tucson.jpg"
        ));

        Trajet tunisSousse = trajetRepository.save(trip(
                tarek,
                corolla,
                "Tunis",
                "Sousse",
                LocalDateTime.now().plusDays(1).withHour(8).withMinute(0).withSecond(0).withNano(0),
                4,
                2,
                18.0,
                145,
                TypeTrajet.LEGER,
                false,
                false,
                1,
                "Cabine",
                StatutTrajet.OUVERT
        ));
        Trajet sfaxTunis = trajetRepository.save(trip(
                nour,
                tucson,
                "Sfax",
                "Tunis",
                LocalDateTime.now().plusDays(2).withHour(7).withMinute(30).withSecond(0).withNano(0),
                5,
                3,
                32.0,
                270,
                TypeTrajet.LONG,
                false,
                true,
                2,
                "Moyen",
                StatutTrajet.OUVERT
        ));
        Trajet tunisHammamet = trajetRepository.save(trip(
                tarek,
                corolla,
                "Tunis",
                "Hammamet",
                LocalDateTime.now().plusDays(3).withHour(17).withMinute(45).withSecond(0).withNano(0),
                4,
                4,
                12.0,
                70,
                TypeTrajet.LEGER,
                false,
                false,
                1,
                "Petit",
                StatutTrajet.OUVERT
        ));
        Trajet sousseMonastir = trajetRepository.save(trip(
                nour,
                tucson,
                "Sousse",
                "Monastir",
                LocalDateTime.now().plusDays(4).withHour(9).withMinute(15).withSecond(0).withNano(0),
                5,
                5,
                8.0,
                25,
                TypeTrajet.LEGER,
                true,
                false,
                0,
                "Sans bagage",
                StatutTrajet.OUVERT
        ));
        Trajet tunisDjerba = trajetRepository.save(trip(
                nour,
                tucson,
                "Tunis",
                "Djerba",
                LocalDateTime.now().plusDays(5).withHour(6).withMinute(30).withSecond(0).withNano(0),
                5,
                1,
                55.0,
                520,
                TypeTrajet.LONG,
                false,
                true,
                2,
                "Grand",
                StatutTrajet.OUVERT
        ));
        Trajet kairouanNabeul = trajetRepository.save(trip(
                tarek,
                corolla,
                "Kairouan",
                "Nabeul",
                LocalDateTime.now().plusDays(6).withHour(14).withMinute(0).withSecond(0).withNano(0),
                4,
                0,
                24.0,
                165,
                TypeTrajet.LONG,
                false,
                false,
                1,
                "Moyen",
                StatutTrajet.COMPLET
        ));
        kairouanNabeul.setNbPlacesDisponibles(0);
        trajetRepository.save(kairouanNabeul);

        Reservation reservationInes = reservationRepository.save(reservation(
                ines,
                tunisSousse,
                1,
                StatutReservation.CONFIRMEE
        ));
        Reservation reservationMalek = reservationRepository.save(reservation(
                malek,
                tunisSousse,
                1,
                StatutReservation.EN_ATTENTE
        ));
        Reservation reservationSalma = reservationRepository.save(reservation(
                salma,
                sfaxTunis,
                2,
                StatutReservation.CONFIRMEE
        ));
        Reservation reservationInesDjerba = reservationRepository.save(reservation(
                ines,
                tunisDjerba,
                2,
                StatutReservation.EN_ATTENTE
        ));
        Reservation reservationMalekComplete = reservationRepository.save(reservation(
                malek,
                kairouanNabeul,
                4,
                StatutReservation.CONFIRMEE
        ));

        avisRepository.save(review(ines, tunisSousse, tarek, 5, "Depart ponctuel depuis Tunis et conduite tres confortable."));
        avisRepository.save(review(salma, sfaxTunis, nour, 5, "Trajet Sfax Tunis bien organise, voiture propre."));
        avisRepository.save(review(malek, kairouanNabeul, tarek, 4, "Bon trajet, conducteur disponible pour les details avant le depart."));

        reclamationRepository.save(reclamation(
                malek,
                reservationMalek,
                "Confirmation en attente",
                "Je souhaite confirmer ma place Tunis Sousse avant la fin de journee.",
                StatutReclamation.OUVERTE
        ));
        reclamationRepository.save(reclamation(
                ines,
                reservationInesDjerba,
                "Question bagage Djerba",
                "Le trajet Tunis Djerba accepte-t-il une valise cabine et un sac a dos ?",
                StatutReclamation.EN_COURS
        ));
        reclamationRepository.save(reclamation(
                salma,
                reservationSalma,
                "Point de rendez-vous",
                "Merci de preciser le point de depart exact a Sfax.",
                StatutReclamation.RESOLUE
        ));
    }

    private Vehicule vehicle(Conducteur conducteur,
                             String marque,
                             String modele,
                             String typeVehicule,
                             String immatriculation,
                             int nbPlaces,
                             String couleur,
                             int annee,
                             String imageUrl) {
        Vehicule vehicule = new Vehicule();
        vehicule.setConducteur(conducteur);
        vehicule.setMarque(marque);
        vehicule.setModele(modele);
        vehicule.setTypeVehicule(typeVehicule);
        vehicule.setImmatriculation(immatriculation);
        vehicule.setNbPlaces(nbPlaces);
        vehicule.setCouleur(couleur);
        vehicule.setAnnee(annee);
        vehicule.setImageUrl(imageUrl);
        return vehicule;
    }

    private Trajet trip(Conducteur conducteur,
                        Vehicule vehicule,
                        String villeDepart,
                        String villeArrivee,
                        LocalDateTime dateDepart,
                        int nbPlacesTotal,
                        int nbPlacesDisponibles,
                        double prix,
                        int distanceKm,
                        TypeTrajet typeTrajet,
                        boolean fumeurAutorise,
                        boolean animauxAutorises,
                        int nbBagagesMax,
                        String typeBagage,
                        StatutTrajet statut) {
        Trajet trajet = new Trajet();
        trajet.setConducteur(conducteur);
        trajet.setVehicule(vehicule);
        trajet.setVilleDepart(villeDepart);
        trajet.setVilleArrivee(villeArrivee);
        trajet.setDateDepart(dateDepart);
        trajet.setNbPlacesTotal(nbPlacesTotal);
        trajet.setNbPlacesDisponibles(nbPlacesDisponibles);
        trajet.setPrix(prix);
        trajet.setDistanceKm(distanceKm);
        trajet.setTypeTrajet(typeTrajet);
        trajet.setFumeurAutorise(fumeurAutorise);
        trajet.setAnimauxAutorises(animauxAutorises);
        trajet.setNbBagagesMax(nbBagagesMax);
        trajet.setTypeBagage(typeBagage);
        trajet.setStatut(statut);
        return trajet;
    }

    private Reservation reservation(Voyageur voyageur, Trajet trajet, int nbPlaces, StatutReservation statut) {
        Reservation reservation = new Reservation();
        reservation.setVoyageur(voyageur);
        reservation.setTrajet(trajet);
        reservation.setNbPlacesReservees(nbPlaces);
        reservation.setStatut(statut);
        return reservation;
    }

    private Avis review(Voyageur auteur, Trajet trajet, Conducteur conducteur, int note, String commentaire) {
        Avis avis = new Avis();
        avis.setAuteur(auteur);
        avis.setTrajet(trajet);
        avis.setConducteur(conducteur);
        avis.setNote(note);
        avis.setCommentaire(commentaire);
        return avis;
    }

    private Reclamation reclamation(Voyageur auteur,
                                    Reservation reservation,
                                    String objet,
                                    String message,
                                    StatutReclamation statut) {
        Reclamation reclamation = new Reclamation();
        reclamation.setAuteur(auteur);
        reclamation.setReservation(reservation);
        reclamation.setObjet(objet);
        reclamation.setMessage(message);
        reclamation.setStatut(statut);
        return reclamation;
    }
}

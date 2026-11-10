package fr.assurance_expargne.demo.Model;

import java.util.Set;

public record Contrat(double cotisation, Set<Integer> sinistres) {}

package fr.assurance_expargne.demo.Model;

import java.util.Set;

public record contrat(double cotisation, Set<Integer> sinistres) {}

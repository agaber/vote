import { Election } from "@/app/model/election";

export interface Choice {
  isEliminated: boolean;
  text: string;
  votesCounted: number;
}

export interface Round {
  counts: Choice[];
}

export interface ElectionResult {
  election: Election;
  rounds: Round[];
  winner: string;
}

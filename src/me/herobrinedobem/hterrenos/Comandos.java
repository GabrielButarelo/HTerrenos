package me.herobrinedobem.hterrenos;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Comandos implements CommandExecutor{
	
	private HTerrenos instance = HTerrenos.getHTerrenos();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p;
		if(sender instanceof Player){
			p = (Player)sender;
			
			if(cmd.getName().equalsIgnoreCase("terreno")){
				if(args.length == 0){
					p.sendMessage("§6Comandos De Terreno:");
					p.sendMessage("§e/terreno desbloquearcomando <comando> §6- Desbloqueia um comando na area");
					p.sendMessage("§e/terreno setmensagem <mensagem> §6- Adiciona uma mensagem de boas vindas");
					p.sendMessage("§e/terreno removeramigo <area> <amigo> §6- Retira um amigo do seu terreno");
					p.sendMessage("§e/terreno addamigo <area> <amigo> §6- Adiciona um amigo no seu terreno");
					p.sendMessage("§e/terreno bloquearcomando <comando> §6- Bloqueia um comando na area");
					p.sendMessage("§e/terreno renomear <areaAntiga> <areaNova> §6- Renomeia um terreno");
					p.sendMessage("§e/terreno pvp <on/off> §6- Ativa ou desativa o pvp no seu terreno");
					p.sendMessage("§e/terreno tp <area> §6- Se teletransporta para um terreno seu");
					p.sendMessage("§e/terreno lista §6- Mostra o nome de todos os seus terrenos");
					p.sendMessage("§e/terreno comprar <tamanho> <area> §6- Compra um terreno");
					p.sendMessage("§e/terreno info §6- Mostra as informacoes do seu terreno");
					p.sendMessage("§e/terreno deletar <area> §6- Deleta o seu terreno");
				}else if(args.length == 3 && (args[0].equalsIgnoreCase("comprar"))){
					try{
						if(p.hasPermission("hterrenos.vip" ) && instance.terrenoUtils.getAreasNumber(p) < 10){
							int tamanho = Integer.parseInt(args[1]);
							String area = args[2];
							if(tamanho == 10 || tamanho == 20 || tamanho == 30 || tamanho == 40 || tamanho == 50 || tamanho == 60 || tamanho == 70 || tamanho == 80 || tamanho == 90 || tamanho == 100){
								instance.terrenoUtils.buyTerrain(p, area, tamanho);
							}else{
								p.sendMessage("§6[Terreno] §eOs tamanhos disponiveis são: 10, 20, 30, 40, 50, 60, 70, 80, 90 e 100!");
								return true;
							}
						}else if(p.hasPermission("hterrenos.notvip" ) && instance.terrenoUtils.getAreasNumber(p) < 5){
							int tamanho = Integer.parseInt(args[1]);
							String area = args[2];
							if(tamanho == 10 || tamanho == 20 || tamanho == 30 || tamanho == 40 || tamanho == 50 || tamanho == 60 || tamanho == 70 || tamanho == 80 || tamanho == 90 || tamanho == 100){
								instance.terrenoUtils.buyTerrain(p, area, tamanho);
							}else{
								p.sendMessage("§6[Terreno] §eOs tamanhos disponiveis são: 10, 20, 30, 40, 50, 60, 70, 80, 90 e 100!");
								return true;
							}
						}else{
							p.sendMessage("§6[Terreno] §eVoce atingiu o seu numero de terrenos disponiveis.");
							return true;
						}
					}catch(NumberFormatException e){
						p.sendMessage("§6[Terreno] §eO tamanho da área é determinado em números!");
						return true;
					}
				}else if(args.length == 2 && (args[0].equalsIgnoreCase("pvp"))){
					String mode = args[1];
					if(mode.equalsIgnoreCase("on")){
						instance.terrenoUtils.setPVPEnable(p, "allow");
					}else if(mode.equalsIgnoreCase("off")){
						instance.terrenoUtils.setPVPEnable(p, "deny");
					}else{
						p.sendMessage("§6[Terreno] §eUse apenas 'on' ou 'off'.");
					}
				}else if(args.length == 2 && (args[0].equalsIgnoreCase("deletar"))){
					String area = p.getName() + "_" + args[1];
					instance.terrenoUtils.deleteTerrain(p, area);
				}else if(args.length == 3 && (args[0].equalsIgnoreCase("addamigo"))){
					String area = p.getName() + "_" + args[0];
					if(instance.getServer().getPlayer(args[1]) != null){
						instance.terrenoUtils.addFriend(p, instance.getServer().getPlayer(args[2]), area);
					}else{
						p.sendMessage("§6[Terreno] §eJogador nao encontrado.");
					}
					instance.terrenoUtils.deleteTerrain(p, area);
				}else if(args.length == 2 && (args[0].equalsIgnoreCase("tp"))){
					String area = p.getName() + "_" + args[1];
					instance.terrenoUtils.tpArea(p, area);
				}else if(args.length > 2 && (args[0].equalsIgnoreCase("setmensagem"))){
					StringBuilder builder = new StringBuilder();
					int i = 0;
					for(String s : args){
						i++;
						if(i >= 2){
							builder.append(s + " ");
						}
					}
					instance.terrenoUtils.setEnterMessage(p, builder.toString());
				}else if(args.length == 2 && (args[0].equalsIgnoreCase("removeramigo"))){
					String area = p.getName() + "_" + args[0];
					instance.terrenoUtils.removeFriend(p, args[1], area);
				}else if(args.length == 1 && (args[0].equalsIgnoreCase("lista"))){
					p.sendMessage("§6[Terreno] §eSuas areas: " + instance.terrenoUtils.getAreas(p));
				}else if(args.length == 4 && (args[0].equalsIgnoreCase("renomear"))){
					instance.terrenoUtils.renameTerrain(p, p.getName() + "_" + args[1] , p.getName() + "_" + args[2]);
				}
			}
		}
		return true;
	}

}

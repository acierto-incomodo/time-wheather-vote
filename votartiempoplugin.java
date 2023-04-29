package com.tu.paquete;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class VotarTiempoPlugin extends JavaPlugin implements Listener {
  
  @Override
  public void onEnable() {
    getLogger().info("Plugin VotarTiempo habilitado!");
    Bukkit.getServer().getPluginManager().registerEvents(this, this);
  }
  
  @Override
  public void onDisable() {
    getLogger().info("Plugin VotarTiempo deshabilitado!");
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("votartiempo")) {
      // Comando para iniciar la votación.
      if (sender instanceof Player) {
        Player player = (Player) sender;
        int numPlayers = Bukkit.getServer().getOnlinePlayers().size();
        int requiredVotes = (int) Math.ceil(numPlayers / 2.0);
        int currentVotes = 0;
        
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
          if (p.getInventory().contains(Material.EMERALD)) {
            currentVotes++;
          }
        }
        
        if (currentVotes >= requiredVotes) {
          // Si se han recibido suficientes votos, cambia el tiempo y el momento del día.
          Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "time set day");
          Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Se ha cambiado el tiempo y el momento del día!");
          
          // También elimina las esmeraldas de los inventarios de los jugadores.
          for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.getInventory().remove(Material.EMERALD);
          }
        } else {
          player.sendMessage(ChatColor.YELLOW + "Faltan votos para cambiar el tiempo y el momento del día!");
        }
      }
      return true;
    }
    return false;
  }
  
  @EventHandler
  public void onPlayerVoteTime(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.DIAMOND_BLOCK) {
      // Si el jugador ha interactuado con el bloque de diamante, añade una esmeralda a su inventario.
      player.getInventory().addItem(new ItemStack(Material.EMERALD, 1));
      player.sendMessage(ChatColor.YELLOW + "Has votado para cambiar el tiempo y el momento del día!");
    }
  }
}

package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;

public class EntityAITasks
{
    private ArrayList tasksToDo;
    private ArrayList field_46137_b;

    public EntityAITasks()
    {
        tasksToDo = new ArrayList();
        field_46137_b = new ArrayList();
    }

    public void addTask(int i, EntityAIBase entityaibase)
    {
        tasksToDo.add(new EntityAITaskEntry(this, i, entityaibase));
    }

    public void onUpdateTasks()
    {
        ArrayList arraylist = new ArrayList();
        Iterator iterator = tasksToDo.iterator();
        do
        {
            if (!iterator.hasNext())
            {
                break;
            }
            EntityAITaskEntry entityaitaskentry = (EntityAITaskEntry)iterator.next();
            boolean flag = field_46137_b.contains(entityaitaskentry);
            if (flag)
            {
                if (!entityaitaskentry.field_46132_a.func_46092_g() || !func_46136_a(entityaitaskentry))
                {
                    entityaitaskentry.field_46132_a.func_46085_d();
                    field_46137_b.remove(entityaitaskentry);
                }
            }
            else if (entityaitaskentry.field_46132_a.func_46090_a() && func_46136_a(entityaitaskentry))
            {
                arraylist.add(entityaitaskentry);
                field_46137_b.add(entityaitaskentry);
            }
        }
        while (true);
        EntityAITaskEntry entityaitaskentry1;
        for (Iterator iterator1 = arraylist.iterator(); iterator1.hasNext(); entityaitaskentry1.field_46132_a.func_46088_e())
        {
            entityaitaskentry1 = (EntityAITaskEntry)iterator1.next();
        }

        EntityAITaskEntry entityaitaskentry2;
        for (Iterator iterator2 = field_46137_b.iterator(); iterator2.hasNext(); entityaitaskentry2.field_46132_a.func_46089_b())
        {
            entityaitaskentry2 = (EntityAITaskEntry)iterator2.next();
        }
    }

    private boolean func_46136_a(EntityAITaskEntry entityaitaskentry)
    {
        label0:
        {
            Iterator iterator = tasksToDo.iterator();
            EntityAITaskEntry entityaitaskentry1;
            label1:
            do
            {
                do
                {
                    do
                    {
                        if (!iterator.hasNext())
                        {
                            break label0;
                        }
                        entityaitaskentry1 = (EntityAITaskEntry)iterator.next();
                    }
                    while (entityaitaskentry1 == entityaitaskentry);
                    if (entityaitaskentry.field_46130_b < entityaitaskentry1.field_46130_b)
                    {
                        continue label1;
                    }
                }
                while (!field_46137_b.contains(entityaitaskentry1) || func_46135_a(entityaitaskentry, entityaitaskentry1));
                return false;
            }
            while (!field_46137_b.contains(entityaitaskentry1) || entityaitaskentry1.field_46132_a.func_46086_f());
            return false;
        }
        return true;
    }

    private boolean func_46135_a(EntityAITaskEntry entityaitaskentry, EntityAITaskEntry entityaitaskentry1)
    {
        return (entityaitaskentry.field_46132_a.func_46091_c() & entityaitaskentry1.field_46132_a.func_46091_c()) == 0;
    }
}

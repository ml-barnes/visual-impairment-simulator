using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MoveBoxBoy : MonoBehaviour {

    public void Tele()
    {
        gameObject.transform.position = new Vector3(
            GetRandomCoordinate(), Random.Range(0.5f, 2), GetRandomCoordinate());

    }

    private float GetRandomCoordinate()
    {
        var co = Random.Range(-7, 7);
        while (co > -1.5 && co < 1.5)
        {
            co = Random.Range(-5, 5);

        }
        return co;


    }
}
